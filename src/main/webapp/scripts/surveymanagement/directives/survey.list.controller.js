'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyListController', function($scope,
    SurveyCollectionResource) {
    var init = function() {
      $scope.pageState = {
        currentPageNumber: 1,
        maxSize: 5,
        totalElements: 0
      };
      $scope.pageChanged();
    };
    $scope.pageChanged = function() {
      $scope.currentPage = SurveyCollectionResource
        .query({
          dataAcquisitionProjectId: $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPageNumber - 1)
        }, function(result) {
          $scope.pageState.totalElements = result.page.totalElements;
        });
    };
    $scope.$on('survey-list-uploaded', function() {
      init();
    });
    init();
  });
