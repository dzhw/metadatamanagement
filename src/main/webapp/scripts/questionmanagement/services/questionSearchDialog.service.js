/* global document*/
'use strict';

angular.module('metadatamanagementApp').service('QuestionSearchDialogService',
  function($mdDialog, blockUI, QuestionSearchResource, CleanJSObjectService) {
    var questions = [];
    var showDialog = function() {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'QuestionSearchDialogController',
        controllerAs: 'questionSearchDialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          questions: questions
        },
        templateUrl: 'scripts/questionmanagement/' +
          'views/questionSearchDialog.html.tmpl',
      });
    };
    var findByProjectId = function(dataAcquisitionProjectId) {
      blockUI.start();
      QuestionSearchResource.findByProjectId(dataAcquisitionProjectId).
      then(function(items) {
        if (!CleanJSObjectService.isNullOrEmpty(items)) {
          questions = items.hits.hits;
          blockUI.stop();
          showDialog();
        } else {
          blockUI.stop();
        }
      });
    };
    var findQuestions = function(ids) {
      blockUI.start();
      QuestionSearchResource.findQuestions(ids).
      then(function(items) {
        if (!CleanJSObjectService.isNullOrEmpty(items)) {
          questions = items.docs;
          blockUI.stop();
          showDialog();
        } else {
          blockUI.stop();
        }
      });
    };
    return {
      findByProjectId: findByProjectId,
      findQuestions: findQuestions
    };
  });
