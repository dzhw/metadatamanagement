/* global document*/
'use strict';

angular.module('metadatamanagementApp').service('QuestionSearchDialogService',
  function($mdDialog) {
    var findQuestions = function(paramObject) {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'QuestionSearchDialogController',
        controllerAs: 'ctrl',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          paramObject: paramObject
        },
        templateUrl: 'scripts/questionmanagement/' +
          'views/questionSearchDialog.html.tmpl',
      });
    };
    return {
      findQuestions: findQuestions
    };
  });
