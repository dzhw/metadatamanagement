/* global document*/
'use strict';

angular.module('metadatamanagementApp').service('StudySearchDialogService',
  function($mdDialog) {
    var findStudies = function(paramObject) {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'StudySearchDialogController',
        controllerAs: 'ctrl',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          paramObject: paramObject
        },
        templateUrl: 'scripts/studymanagement/' +
          'views/studySearchDialog.html.tmpl'
      });
    };
    return {
      findStudies: findStudies
    };
  });
