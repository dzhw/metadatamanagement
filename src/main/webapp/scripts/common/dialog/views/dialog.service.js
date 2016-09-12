/* global document*/

'use strict';
angular.module('metadatamanagementApp').service('DialogService',
  function($mdDialog, blockUI, QuestionReferencedResource,
    VariableReferencedResource,
    SurveyReferencedResource, DataSetReferencedResource, $rootScope) {
    var showDialog = function(ids, type) {
      var entityResources = [];
      var dialogParent = angular.element(document.body);
      var idsAsString = '"' + ids + '"';
      idsAsString = idsAsString.replace(/[\[\]'"]/g, '');
      var openDialog = function() {
        blockUI.stop();
        $mdDialog.show({
          controller: 'DialogController',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            entities: entityResources,
            currentLanguage: $rootScope.currentLanguage,
            type: type
          },
          templateUrl: 'scripts/common/dialog/views/dialog.html.tmpl',
        });
      };
      var createCustomEntities = function(customItem, id) {
        var rows = [];
        var entity = {};
        for (var key in customItem) {
          if (key !== '_links') {
            if ((customItem[key].hasOwnProperty('en')) ||
            (customItem[key].hasOwnProperty('de'))) {
              var obj = customItem[key];
              rows.push(obj[$rootScope.currentLanguage]);
            }else {
              rows.push(customItem[key]);
            }
          }
        }
        entity.id = id;
        entity.found = true;
        entity.type = type + 'Detail';
        entity.hasNoReferences = false;
        entity.rows = rows;
        entityResources.push(entity);
      };
      var checkInvalidIds = function(customItems) {
        if (ids instanceof Array) {
          ids.forEach(function(id) {
            if (customItems.length > 0) {
              for (var i = 0; i < customItems.length; i++) {
                if (customItems[i].id === id) {
                  createCustomEntities(customItems[i], id);
                  break;
                }else {
                  if (i === (customItems.length - 1)) {
                    entityResources.push({
                      id: id,
                      found: false,
                      hasNoReferences: false
                    });
                  }
                }
              }
            } else {
              entityResources.push({
                id: id,
                found: false,
                hasNoReferences: false
              });
            }
          });
        } else {
          if (customItems.length > 0) {
            for (var i = 0; i < customItems.length; i++) {
              createCustomEntities(customItems[i], customItems[i].id);
            }
          } else {
            entityResources.push({
              id: ids,
              found: false,
              hasNoReferences: true
            });
          }
        }
        openDialog();
      };
      switch (type) {
        case 'variable': VariableReferencedResource
        .findByQuestionId({id: ids})
          .$promise.then(function(customVariables) {
            checkInvalidIds(customVariables._embedded.variables);
          });
          break;
        case 'survey': SurveyReferencedResource
        .findByIdIn({ids: idsAsString})
          .$promise.then(function(customSurveys) {
            checkInvalidIds(customSurveys._embedded.surveys);
          });
          break;
        case 'data-set': DataSetReferencedResource
        .findByIdIn({ids: idsAsString})
          .$promise.then(function(customDataSets) {
            checkInvalidIds(customDataSets._embedded.dataSets);
          });
          break;
        case 'question': QuestionReferencedResource
        .findByIdIn({ids: idsAsString})
         .$promise.then(function(customQuestions) {
            checkInvalidIds(customQuestions._embedded.questions);
          });
          break;
        default: console.log(type + ' not implemented');
          break;
      }
    };
    return {
      showDialog: showDialog
    };
  });
