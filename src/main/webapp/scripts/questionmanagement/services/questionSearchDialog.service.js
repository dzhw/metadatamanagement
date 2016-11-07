/* global document*/
'use strict';

angular.module('metadatamanagementApp').service('QuestionSearchDialogService',
  function($mdDialog) {
    var findQuestions = function(methodName, methodParams, count) {
      var dialogParent = angular.element(document.body);
      $mdDialog.show({
        controller: 'QuestionSearchDialogController',
        controllerAs: 'QuestionSearchDialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          methodName: methodName,
          methodParams: methodParams,
          count: count
        },
        templateUrl: 'scripts/questionmanagement/' +
          'views/questionSearchDialog.html.tmpl',
      });
    };
    return {
      findQuestions: findQuestions
    };
  });
