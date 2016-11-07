/* global document*/
'use strict';

angular.module('metadatamanagementApp').service('StudySearchDialogService',
  function($mdDialog) {
    var findStudies = function(methodName, methodParams, count) {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'StudySearchDialogController',
        controllerAs: 'StudySearchDialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          methodName: methodName,
          methodParams: methodParams,
          count: count
        },
        templateUrl: 'scripts/studymanagement/' +
          'views/studySearchDialog.html.tmpl',
      });
    };
    return {
      findStudies: findStudies
    };
  });
