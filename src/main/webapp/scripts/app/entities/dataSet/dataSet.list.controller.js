'use strict';

angular.module('metadatamanagementApp')
    .controller('DataSetListController', function($scope, DataSetsCollection) {
      $scope.init = function() {
        $scope.pageState = {
          currentPageNumber: 1,
          totalElements: 0
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        $scope.currentPage = DataSetsCollection.query({dataAcquisitionProjectId:
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
