'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableListController', function($scope,
      VariableCollection) {
      var init = function() {
        $scope.pageState = {
          maxSize: 5,
          currentPage: 1,
          totalElements: 0
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        $scope.currentPage = VariableCollection.query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPage - 1)
        });
      };
      $scope.$on('refresh', function() {
        init();
      });
      $scope.$watch('currentPage', function(currentPage) {
        if (currentPage.$resolved) {
          $scope.pageState.totalElements = currentPage.page.totalElements;
        }
      }, true);
      init();
    });
