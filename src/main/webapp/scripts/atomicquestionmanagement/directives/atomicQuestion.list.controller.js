'use strict';

angular.module('metadatamanagementApp')
  .controller('AtomicQuestionListController', function($scope,
    AtomicQuestionCollectionResource) {
    $scope.init = function() {
      $scope.pageState = {
        currentPageNumber: 1,
        maxSize: 5,
        totalElements: 0
      };
      $scope.pageChanged();
    };
    $scope.pageChanged = function() {
      $scope.currentPage = AtomicQuestionCollectionResource.
      query({
        dataAcquisitionProjectId: $scope.params.dataAcquisitionProjectId,
        page: ($scope.pageState.currentPageNumber - 1)
      }, function(result) {
        $scope.pageState.totalElements = result.page.totalElements;
      });
    };
    $scope.$on('atomicQuestion-list-uploaded', function() {
      $scope.init();
    });
    $scope.init();
  });
