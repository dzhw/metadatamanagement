'use strict';

angular.module('metadatamanagementApp').directive('surveySearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'survey-search-result.html.tmpl',
      scope: {
        searchQuery: '<',
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        addMargin: '=',
        searchResultIndex: '=',
        setParams: '&'
      },
      controller: function($scope, CommonDialogsService, SurveyResource,
        ElasticSearchAdminService, $rootScope, SimpleMessageToastService,
        DataAcquisitionProjectResource, Principal, ProjectUpdateAccessService,
        $state, $q, $timeout, $element, HighlightService) {
        $scope.projectIsCurrentlyReleased = true;
        if ($scope.searchQuery) {
          $timeout(function() {
            HighlightService.apply($element[0], $scope.searchQuery);
          });
        }
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
                  processUpdateQueue('data_packages')
              ]).promise;
            }).then(function() {
              $rootScope.$broadcast('deletion-completed');
              SimpleMessageToastService.openSimpleMessageToast(
                'survey-management.edit.survey-deleted-toast',
                {id: surveyId});
            });
          }
        };

        $scope.surveyEdit = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            $scope.project,
            'surveys',
            true
          )) {
            $state.go('surveyEdit', {id: $scope.searchResult.id});
          }
        };
        $scope.isLoggedIn = Principal.loginName();

      }
    };
  });
