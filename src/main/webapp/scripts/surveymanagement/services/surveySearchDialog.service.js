/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('SurveySearchDialogService',
    function($mdDialog) {
      var findSurveys = function(paramObject) {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'SurveySearchDialogController',
          controllerAs: 'ctrl',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            paramObject: paramObject
          },
          templateUrl: 'scripts/surveymanagement/views/' +
            'surveySearchDialog.html.tmpl',
        });
      };
      return {
        findSurveys: findSurveys
      };
    });
