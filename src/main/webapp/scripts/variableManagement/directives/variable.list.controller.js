'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableListController', function($scope,
      VariableCollection) {
      var init = function() {
        $scope.pageState = {
          currentPageNumber: 1,
          maxSize: 5,
          totalElements: 0
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        $scope.currentPage = VariableCollection.query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPageNumber - 1)
        }, function(result) {
          $scope.pageState.totalElements = result.page.totalElements;
        });
      };
      $scope.$on('variable-list-uploaded', function() {
        init();
      });
      init();
    });
