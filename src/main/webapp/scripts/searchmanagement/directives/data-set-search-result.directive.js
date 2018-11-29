'use strict';

angular.module('metadatamanagementApp').directive('datasetSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'data-set-search-result.html.tmpl',
      scope: {
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        addMargin: '=',
        searchResultIndex: '=',
        isUpdateAllowed: '=?'
      },
      controller: function($scope, CommonDialogsService, DataSetResource,
        ElasticSearchAdminService, $rootScope, SimpleMessageToastService,
        DataAcquisitionProjectResource, Principal) {
        $scope.projectIsCurrentlyReleased = true;
        if (angular.isUndefined($scope.isUpdateAllowed)) {
          $scope.isUpdateAllowed = true;
        }
        if (Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: $scope.searchResult.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            $scope.projectIsCurrentlyReleased = (project.release != null);
          });
        }
        $scope.deleteDataSet = function(dataSetId) {
          CommonDialogsService.showConfirmDeletionDialog({
            type: 'data-set',
            id: dataSetId
          }).then(function() {
            return DataSetResource.delete({id: dataSetId}).$promise;
          }).then(function() {
            return ElasticSearchAdminService.processUpdateQueue('data_sets');
          }).then(function() {
            $rootScope.$broadcast('deletion-completed');
            SimpleMessageToastService.openSimpleMessageToast(
              'data-set-management.edit.data-set-deleted-toast',
              {id: dataSetId});
          });
        };
      }
    };
  });
