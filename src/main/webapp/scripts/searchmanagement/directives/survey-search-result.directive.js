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
        ElasticSearchAdminService, $rootScope, SimpleMessageToastService,
        DataAcquisitionProjectResource, Principal, ProjectUpdateAccessService,
        $transitions, $q) {
        $scope.projectIsCurrentlyReleased = true;
        if (Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: $scope.searchResult.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            $scope.project = project;
            $scope.projectIsCurrentlyReleased = (project.release != null);
          });
        }

        $scope.deleteSurvey = function(surveyId) {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            $scope.project,
            'surveys',
            true
          )) {
            CommonDialogsService.showConfirmDeletionDialog({
              type: 'survey',
              id: surveyId
            }).then(function() {
              return SurveyResource.delete({id: surveyId}).$promise;
            }).then(function() {
              return $q.all([
                ElasticSearchAdminService.
                  processUpdateQueue('surveys'),
                ElasticSearchAdminService.
                  processUpdateQueue('studies'),
              ]).promise;
            }).then(function() {
              $rootScope.$broadcast('deletion-completed');
              SimpleMessageToastService.openSimpleMessageToast(
                'survey-management.edit.survey-deleted-toast',
                {id: surveyId});
            });
          }
        };

        var unregisterTransitionHook = $transitions.onBefore({to: 'surveyEdit'},
          function() {
            if (!ProjectUpdateAccessService.isUpdateAllowed(
              $scope.project,
              'surveys',
              true
            )) {
              return false;
            }
          });
        $scope.$on('$destroy', unregisterTransitionHook);
      }
    };
  });
