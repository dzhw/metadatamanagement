'use strict';

angular.module('metadatamanagementApp').directive('studySearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'study-search-result.html.tmpl',
      scope: {
        searchQuery: '<',
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '='
      },
      controller: function($scope, DataAcquisitionProjectResource,
        Principal, ProjectUpdateAccessService, $state) {
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
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.studyEdit = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            $scope.project,
            'studies',
            true
          )) {
            $state.go('studyEdit', {id: $scope.searchResult.id});
          }
        };
        $scope.isLoggedIn = Principal.loginName();
      }
    };
  });
