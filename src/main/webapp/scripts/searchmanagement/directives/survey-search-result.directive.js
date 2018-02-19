'use strict';

angular.module('metadatamanagementApp').directive('surveySearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'survey-search-result.html.tmpl',
      scope: {
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        addMargin: '=',
        searchResultIndex: '='
      },
      controller: function($scope, CommonDialogsService, SurveyResource,
        ElasticSearchAdminService, $rootScope, SimpleMessageToastService) {
        $scope.deleteSurvey = function(surveyId) {
          CommonDialogsService.showConfirmDeletionDialog({
            type: 'survey',
            id: surveyId
          }).then(function() {
            return SurveyResource.delete({id: surveyId}).$promise;
          }).then(function() {
            return ElasticSearchAdminService.processUpdateQueue('surveys');
          }).then(function() {
            $rootScope.$broadcast('deletion-completed');
            SimpleMessageToastService.openSimpleMessageToast(
              'survey-management.edit.survey-deleted-toast',
              {id: surveyId}, true);
          });
        };
      }
    };
  });
