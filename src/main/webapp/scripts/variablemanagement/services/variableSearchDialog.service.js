/* global document*/

'use strict';
angular.module('metadatamanagementApp').service('VariableSearchDialogService',
      function($mdDialog) {
        var findVariables = function(methodName, methodParams, count) {
          var dialogParent = angular.element(document.body);
          $mdDialog.show({
            controller: 'VariableSearchDialogController',
            controllerAs: 'VariableSearchDialogController',
            parent: dialogParent,
            clickOutsideToClose: true,
            locals: {
              methodName: methodName,
              methodParams: methodParams,
              count: count
            },
            templateUrl: 'scripts/variablemanagement/' +
              'views/variableSearchDialog.html.tmpl',
          });
        };
        return {
          findVariables: findVariables
        };
      });
