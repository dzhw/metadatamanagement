'use strict';

angular.module('metadatamanagementApp')
    .controller('SurveyListController', function($scope, SurveyCollection) {
      var init = function() {
        $scope.pageState = {
          currentPageNumber: 1,
          totalElements: 0
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        $scope.pageState.loadingState = true;
        $scope.currentPage = SurveyCollection.query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPageNumber - 1)
        }, function(result) {
          $scope.pageState.totalElements = result.page.totalElements;
        });
      };
      $scope.$on('refresh', function() {
        init();
      });
      init();
    });
