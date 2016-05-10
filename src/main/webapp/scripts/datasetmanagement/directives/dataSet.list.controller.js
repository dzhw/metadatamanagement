'use strict';

angular.module('metadatamanagementApp')
    .controller('DataSetListController', function($scope,
      DataSetCollectionResource) {
      $scope.init = function() {
        $scope.pageState = {
          currentPageNumber: 1,
          maxSize: 5,
          totalElements: 0
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        $scope.currentPage = DataSetCollectionResource
        .query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPageNumber - 1)
        }, function(result) {
          $scope.pageState.totalElements = result.page.totalElements;
        });
      };
      $scope.$on('dataSet-list-uploaded', function() {
        $scope.init();
      });

      $scope.init();
    });
