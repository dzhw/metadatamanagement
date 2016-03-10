'use strict';

angular.module('metadatamanagementApp')
    .controller('DataSetsListController', function($scope, DataSetsCollection) {
      $scope.init = function() {
        $scope.pageState = {
          promiseParam: false,
          maxSize: 5,
          currentPage: 1,
          bigTotalItems: 0,
          dataSets: []
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        DataSetsCollection.query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPage - 1),
        }, function(result) {
          $scope.pageState.promiseParam = true;
          $scope.pageState.dataSets = result._embedded.dataSets;
          $scope.pageState.bigTotalItems = result.page.totalElements;
          $scope.$emit('dataSetsLength', $scope.pageState.bigTotalItems);
        });
      };
      $scope.$on('refresh', function() {
        $scope.init();
      });
      $scope.init();
    });
