/* global document*/

'use strict';
angular.module('metadatamanagementApp').service('VariableSearchDialogService',
      function($mdDialog) {
        var findVariables = function(paramObject) {
          var dialogParent = angular.element(document.body);
          $mdDialog.show({
            controller: 'VariableSearchDialogController',
            controllerAs: 'ctrl',
            parent: dialogParent,
            clickOutsideToClose: true,
            locals: {
              paramObject: paramObject
            },
            templateUrl: 'scripts/variablemanagement/' +
              'views/variableSearchDialog.html.tmpl'
          });
        };
        return {
          findVariables: findVariables
        };
      });
