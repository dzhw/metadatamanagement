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
          page: ($scope.pageState.currentPage - 1),
        },function(result) {
          $scope.pageState.variables = result._embedded.variables;
          $scope.pageState.totalElements = result.page.totalElements;
        });
      };
      $scope.$on('refresh', function() {
        init();
      });
      init();
    });
