/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('SurveySearchDialogService',
    function($mdDialog) {
      var findSurveys = function(methodName, methodParams, count, surveyId) {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'SurveySearchDialogController',
          controllerAs: 'ctrl',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            methodName: methodName,
            methodParams: methodParams,
            count: count,
            surveyId: surveyId
          },
          templateUrl: 'scripts/surveymanagement/views/' +
            'surveySearchDialog.html.tmpl',
        });
      };
      return {
        findSurveys: findSurveys
      };
    });
