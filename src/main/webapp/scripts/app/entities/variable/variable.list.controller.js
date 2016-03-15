'use strict';

angular.module('metadatamanagementApp')
    .controller('VariableListController', function($scope,
      VariableCollection) {
      var init = function() {
        $scope.pageState = {
          currentPageNumber: 1,
          totalElements: 0
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        $scope.currentPage = VariableCollection.query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPageNumber - 1)
        });
      };
      $scope.$on('variables-uploaded', function() {
        init();
      });
      init();
    });
