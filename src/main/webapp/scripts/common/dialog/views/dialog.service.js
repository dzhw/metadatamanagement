/* global document */
'use strict';

angular.module('metadatamanagementApp').service('DialogService',
  function($mdDialog, blockUI, $resource) {
    var showDialog = function(entities, type, toBeDisplayedInformations,
      currentLanguage) {
      var entitiesResources = [];
      var dialogParent = angular.element(document.body);
      /* number of load sequences */
      var counter = 0;

      /* function to load resources sequentially */
      var loadEntities = function() {
        if (counter < entities.length) {
          $resource('api/' + type + 's/:id').get({
            id: entities[counter],
            projection: 'complete'
          }).$promise.then(function(resource) {
            var rows = [];
            var entity = {};
            toBeDisplayedInformations.forEach(function(rowName) {
              if ((resource[rowName].hasOwnProperty('en')) ||
              (resource[rowName].hasOwnProperty('de'))) {
                var obj = resource[rowName];
                rows.push(obj[currentLanguage]);
              } else {
                rows.push(resource[rowName]);
              }
            });
            entity.id = resource.id;
            entity.found = true;
            entity.type = type + 'Detail';
            entity.rows = rows;
            entitiesResources.push(entity);
            counter++;
            loadEntities();
          }, function() {
            var resourceNotFound = {
              id: entities[counter],
              found: false
            };
            entitiesResources.push(resourceNotFound);
            counter++;
            loadEntities();
          });
        } else {
          blockUI.stop();
          $mdDialog.show({
            controller: 'DialogController',
            parent: dialogParent,
            clickOutsideToClose: true,
            locals: {
              entities: entitiesResources,
              currentLanguage: currentLanguage,
              type: type
            },
            templateUrl: 'scripts/common/dialog/views/dialog.html.tmpl',
          });
        }
      };
      loadEntities();
    };
    return {
      showDialog: showDialog
    };
  });
