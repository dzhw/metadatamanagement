'use strict';

angular.module('metadatamanagementApp')
    .controller('DataSetListController', function($scope, DataSetsCollection) {
      $scope.init = function() {
        $scope.pageState = {
          maxSize: 5,
          currentPage: 1,
          totalElements: 0
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        $scope.currentPage = DataSetsCollection.query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPage - 1)
        });
      };
      $scope.$on('refresh', function() {
        $scope.init();
      });
      $scope.$watch('currentPage', function(currentPage) {
        if (currentPage.$resolved) {
          $scope.pageState.totalElements = currentPage.page.totalElements;
        }
      }, true);
      $scope.init();
    });
