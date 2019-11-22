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
        Principal, ProjectUpdateAccessService, $state) {
        this.projectIsCurrentlyReleased = true;
        if (Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: this.searchResult.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            this.project = project;
            this.projectIsCurrentlyReleased = (project.release != null);
          });
        }
        this.isAuthenticated = Principal.isAuthenticated;
        this.studyEdit = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            this.project,
            'studies',
            true
          )) {
            $state.go('studyEdit', {id: this.searchResult.id});
          }
        };
        this.isLoggedIn = Principal.loginName();
      }
    };
  });
