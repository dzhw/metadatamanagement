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
        searchResultIndex: '=',
        setParams: '&'
      },
      controllerAs: '$ctrl',
      bindToController: true,
      controller: function(DataAcquisitionProjectResource,
        Principal, ProjectUpdateAccessService, $state, $element, $timeout,
        HighlightService) {
        var ctrl = this;
        ctrl.projectIsCurrentlyReleased = true;
        ctrl.$onInit = init;

        function init() {
          if (ctrl.searchQuery) {
            $timeout(function() {
              HighlightService.apply($element[0], ctrl.searchQuery);
            });
          }
          if (Principal
              .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
            DataAcquisitionProjectResource.get({
              id: ctrl.searchResult.dataAcquisitionProjectId
            }).$promise.then(function(project) {
              ctrl.project = project;
              ctrl.projectIsCurrentlyReleased = (project.release != null);
            });
          }
        }
        ctrl.isAuthenticated = Principal.isAuthenticated;
        ctrl.studyEdit = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            ctrl.project,
            'studies',
            true
          )) {
            $state.go('studyEdit', {id: ctrl.searchResult.id});
          }
        };
        ctrl.isLoggedIn = Principal.loginName();
      }
    };
  });
