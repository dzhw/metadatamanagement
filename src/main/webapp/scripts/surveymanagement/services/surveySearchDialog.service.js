/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('SurveySearchDialogService',
    function($mdDialog) {
      var findSurveys = function(methodName, methodParams) {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'SurveySearchDialogController',
          controllerAs: 'SurveySearchDialogController',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            methodName: methodName,
            methodParams: methodParams
          },
          templateUrl: 'scripts/surveymanagement/views/' +
            'surveySearchDialog.html.tmpl',
        });
      };
      return {
        findSurveys: findSurveys
      };
    });
