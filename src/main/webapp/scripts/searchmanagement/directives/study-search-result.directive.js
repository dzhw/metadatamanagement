'use strict';

angular.module('metadatamanagementApp').directive('studySearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'study-search-result.html.tmpl',
      scope: {
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '=',
        isUpdateAllowed: '=?'
      },
      controller: function($scope, DataAcquisitionProjectResource,
        Principal, ProjectUpdateAccessService, $transitions) {
        $scope.projectIsCurrentlyReleased = true;
        if (angular.isUndefined($scope.isUpdateAllowed)) {
          $scope.isUpdateAllowed = true;
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
        var unregisterTransitionHook = $transitions.onBefore({to: 'studyEdit'},
          function() {
            if (!ProjectUpdateAccessService.isUpdateAllowed(
              $scope.project,
              'studies',
              true
            )) {
              return Promise.reject();
            }
          });
        $scope.$on('$destroy', unregisterTransitionHook);
      }
    };
  });
