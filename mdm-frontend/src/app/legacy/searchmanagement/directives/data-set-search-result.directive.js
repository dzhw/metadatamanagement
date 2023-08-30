'use strict';

angular.module('metadatamanagementApp').directive('datasetSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'data-set-search-result.html.tmpl',
      scope: {
        searchQuery: '<',
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        addMargin: '=',
        searchResultIndex: '=',
        setParams: '&'
      },
      controller: ['$scope', 'CommonDialogsService', 'DataSetResource',
        'ElasticSearchAdminService', '$rootScope', 'SimpleMessageToastService',
        'DataAcquisitionProjectResource', 'Principal', 'ProjectUpdateAccessService',
        '$state', '$q', '$timeout', '$element', 'HighlightService',
        function($scope, CommonDialogsService, DataSetResource,
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
        $scope.deleteDataSet = function(dataSetId) {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            $scope.project,
            'data_sets',
            true
          )) {
            CommonDialogsService.showConfirmDeletionDialog({
              type: 'data-set',
              id: dataSetId
            }).then(function() {
              return DataSetResource.delete({id: dataSetId}).$promise;
            }).then(function() {
              return $q.all([
                ElasticSearchAdminService.
                  processUpdateQueue('data_sets'),
                ElasticSearchAdminService.
                  processUpdateQueue('data_packages')
              ]).promise;
            }).then(function() {
              $rootScope.$broadcast('deletion-completed');
              SimpleMessageToastService.openSimpleMessageToast(
                'data-set-management.edit.data-set-deleted-toast',
                {id: dataSetId});
            });
          }
        };

        $scope.dataSetEdit = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            $scope.project,
            'data_sets',
            true
          )) {
            $state.go('dataSetEdit', {id: $scope.searchResult.id});
          }
        };
        $scope.isLoggedIn = Principal.loginName();
      }]
    };
  });
