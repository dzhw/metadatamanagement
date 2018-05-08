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
        searchResultIndex: '='
      },
      controller: function($scope, DataAcquisitionProjectResource,
        Principal) {
        $scope.projectIsCurrentlyReleased = true;
        if (Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: $scope.searchResult.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            $scope.projectIsCurrentlyReleased = (project.release != null);
          });
        }
      }
    };
  });
