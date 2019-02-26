'use strict';

angular.module('metadatamanagementApp').directive('instrumentSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'instrument-search-result.html.tmpl',
      scope: {
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '='
      },
      controller: function($scope, CommonDialogsService, InstrumentResource,
        ElasticSearchAdminService, $rootScope, SimpleMessageToastService,
        DataAcquisitionProjectResource, Principal, ProjectUpdateAccessService,
        $state) {
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
        $scope.deleteInstrument = function(instrumentId) {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            $scope.project,
            'instruments',
            true
          )) {
            CommonDialogsService.showConfirmDeletionDialog({
              type: 'instrument',
              id: instrumentId
            }).then(function() {
              return InstrumentResource.delete({id: instrumentId}).$promise;
            }).then(function() {
              return ElasticSearchAdminService.
                processUpdateQueue('instruments');
            }).then(function() {
              $rootScope.$broadcast('deletion-completed');
              SimpleMessageToastService.openSimpleMessageToast(
                'instrument-management.edit.instrument-deleted-toast',
                {id: instrumentId});
            });
          }
        };
        $scope.instrumentEdit = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            $scope.project,
            'instruments',
            true
          )) {
            $state.go('instrumentEdit', {id: $scope.searchResult.id});
          }
        };
        $scope.isLoggedIn = Principal.loginName();
      }
    };
  });
