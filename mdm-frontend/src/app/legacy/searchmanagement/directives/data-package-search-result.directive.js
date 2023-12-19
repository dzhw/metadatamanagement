'use strict';

angular.module('metadatamanagementApp').directive('datapackageSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'data-package-search-result.html.tmpl',
      scope: {
        searchQuery: '<',
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '=',
        setParams: '&'
      },
      controllerAs: '$ctrl',
      bindToController: true,
      controller: ['DataAcquisitionProjectResource',
        'Principal', 'ProjectUpdateAccessService', '$state', '$element', '$timeout',
        'HighlightService',
        function(DataAcquisitionProjectResource,
        Principal, ProjectUpdateAccessService, $state, $element, $timeout,
        HighlightService) {
        var ctrl = this;
        ctrl.projectIsCurrentlyReleased = true;
        ctrl.$onInit = init;

        function init() {
          ctrl.query = ctrl.searchQuery;
          if (ctrl.searchQuery) {
            $timeout(function() {
              HighlightService.apply($element[0], ctrl.searchQuery,
                ['ngTruncateToggleText', 'md-card-search-action']);
            });
          }
          if (Principal
              .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
            DataAcquisitionProjectResource.get({
              id: ctrl.searchResult.dataAcquisitionProjectId
            }).$promise.then(function(project) {
              ctrl.project = project;
              ctrl.projectIsCurrentlyReleased = (project.release != null && !project.release.isPreRelease);
            });
          }
        }
        ctrl.isAuthenticated = Principal.isAuthenticated;
        ctrl.dataPackageEdit = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            ctrl.project,
            'dataPackages',
            true
          )) {
            $state.go('dataPackageEdit', {id: ctrl.searchResult.id});
          }
        };
        ctrl.isLoggedIn = Principal.loginName();
      }]
    };
  });
