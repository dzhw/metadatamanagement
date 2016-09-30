/* global document*/

'use strict';
angular.module('metadatamanagementApp').service('QuestionSearchDialogService',
  function($mdDialog, blockUI, QuestionSearchResource, CleanJSObjectService) {
    var questions = [];
    var showDialog = function() {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'QuestionSearchDialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          questions: questions
        },
        templateUrl: 'scripts/questionmanagement/' +
        'directives/questionSearchDialog.html.tmpl',
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
    return {
      findByProjectId: findByProjectId
    };
  });
