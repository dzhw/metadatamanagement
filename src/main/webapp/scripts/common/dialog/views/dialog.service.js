/* global document */
'use strict';

angular.module('metadatamanagementApp').service('DialogService',
  function($mdDialog, blockUI, $resource) {
    var showDialog = function(variables, currentLanguage) {
      var variableResources = [];
      var dialogParent = angular.element(document.body);
      /* number of load sequences */
      var counter = 0;

      /* function to load successors sequentially */
      var loadVariables = function() {
        if (counter < variables.length) {
          $resource('api/variables/:id').get({
            id: variables[counter],
            projection: 'complete'
          }).$promise.then(function(resource) {
            variableResources.push(resource);
            counter++;
            loadVariables();
          }, function() {
            var notFoundVariable = {
              id: variables[counter],
              name: 'notFoundVariable'
            };
            variableResources.push(notFoundVariable);
            counter++;
            loadVariables();
          });
        } else {
          blockUI.stop();
          $mdDialog.show({
            controller: 'DialogController',
            parent: dialogParent,
            clickOutsideToClose: true,
            locals: {
              variables: variableResources,
              currentLanguage: currentLanguage
            },
            templateUrl: 'scripts/common/dialog/views/dialog.html.tmpl',
          });
        }
      };
      loadVariables();
    };
    return {
      showDialog: showDialog
    };
  });
