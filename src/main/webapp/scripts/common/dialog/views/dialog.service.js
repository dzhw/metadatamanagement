/* global document*/

'use strict';
angular.module('metadatamanagementApp').service('DialogService',
  function($mdDialog, blockUI, QuestionSearchResource, VariableSearchResource,
    SurveySearchResource, DataSetSearchResource, $rootScope) {
    var showDialog = function(ids, type) {
      var entityResources = [];
      var dialogParent = angular.element(document.body);
      var idsAsString = '"' + ids + '"';
      idsAsString = idsAsString.replace(/[\(\)\[\]{}'"]/g, '');
      var dialogConfig = function() {
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
      var checkInvalidIds = function(customItems) {
        ids.forEach(function(id) {
          for (var i = 0; i < customItems.length; i++) {
            if (customItems[i].id === id) {
              var rows = [];
              var entity = {};
              for (var key in customItems[i]) {
                if (key !== '_links') {
                  if ((customItems[i][key].hasOwnProperty('en')) ||
                  (customItems[i][key].hasOwnProperty('de'))) {
                    var obj = customItems[i][key];
                    rows.push(obj[$rootScope.currentLanguage]);
                  }else {
                    rows.push(customItems[i][key]);
                  }
                }
              }
              entity.id = id;
              entity.found = true;
              entity.type = type + 'Detail';
              entity.rows = rows;
              entityResources.push(entity);
              break;
            }else {
              if (i === (customItems.length - 1)) {
                var resourceNotFound = {
                  id: id,
                  found: false
                };
                entityResources.push(resourceNotFound);
              }
            }
          }
        });
      };

      switch (type) {
        case 'variable': VariableSearchResource.findByIdIn({ids: idsAsString})
          .$promise.then(function(customVariables) {
            checkInvalidIds(customVariables._embedded.variables);
            dialogConfig();
          });
          break;
        case 'survey': SurveySearchResource.findByIdIn({ids: idsAsString})
          .$promise.then(function(customSurveys) {
            checkInvalidIds(customSurveys._embedded.surveys);
            dialogConfig();
          });
          break;
        case 'data-set': DataSetSearchResource.findByIdIn({ids: idsAsString})
          .$promise.then(function(customDataSets) {
            checkInvalidIds(customDataSets._embedded.dataSets);
            dialogConfig();
          });
          break;
        case 'question': QuestionSearchResource.findByIdIn({ids: idsAsString})
         .$promise.then(function(customQuestions) {
            checkInvalidIds(customQuestions._embedded.questions);
            dialogConfig();
          });
          break;
        default: console.log('nsnsnd');
          break;
      }
    };
    return {
      showDialog: showDialog
    };
  });
