'use strict';

angular.module('metadatamanagementApp')
    .controller('VariablesListController', function($scope,
      VariableCollection) {
      var init = function() {
        $scope.pageState = {
          promiseParam: false,
          maxSize: 5,
          currentPage: 1,
          bigTotalItems: 0,
          surveys: []
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        VariableCollection.query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPage - 1),
        }, function(result) {
          $scope.pageState.promiseParam = true;
          $scope.pageState.variables = result._embedded.variables;
          $scope.pageState.bigTotalItems = result.page.totalElements;
          $scope.$emit('variablesLength', $scope.pageState.bigTotalItems);
        });
      };
      $scope.$on('refresh', function() {
        init();
      });
      init();
    });
