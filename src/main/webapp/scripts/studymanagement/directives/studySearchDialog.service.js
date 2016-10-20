/* global document*/
'use strict';

angular.module('metadatamanagementApp').service('StudySearchDialogService',
  function($mdDialog, blockUI, StudySearchResource, CleanJSObjectService) {
    var studies = [];
    var showDialog = function() {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'StudySearchDialogController',
        controllerAs: 'ctrl',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          studies: studies
        },
        templateUrl: 'scripts/studymanagement/' +
          'directives/studySearchDialog.html.tmpl',
      });
    };
    var findStudies = function(ids) {
      blockUI.start();
      StudySearchResource.findStudies(ids).
      then(function(items) {
        if (!CleanJSObjectService.isNullOrEmpty(items)) {
          studies = items.docs;
          blockUI.stop();
          showDialog();
        } else {
          blockUI.stop();
        }
      });
    };
    return {
      findStudies: findStudies
    };
  });
