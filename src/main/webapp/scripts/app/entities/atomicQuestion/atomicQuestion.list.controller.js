'use strict';

angular.module('metadatamanagementApp')
    .controller('AtomicQuetionListController', function($scope,
      AtomicQuestionCollection) {
      $scope.init = function() {
        $scope.pageState = {
          currentPageNumber: 1,
          totalElements: 0
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        $scope.currentPage = AtomicQuestionCollection.
        query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPageNumber - 1)
        }, function(result) {
          $scope.pageState.totalElements = result.page.totalElements;
        });
      };
      $scope.$on('atomicQuestions-uploaded', function() {
        $scope.init();
      });
      $scope.init();
    });
