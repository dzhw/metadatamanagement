'use strict';

angular.module('metadatamanagementApp')
    .controller('SurveyListController', function($scope, SurveyCollection) {
      var init = function() {
        $scope.pageState = {
          promiseParam: false,
          maxSize: 5,
          currentPage: 1,
          totalElements: 0
        };
        $scope.pageChanged();
      };
      $scope.pageChanged = function() {
        $scope.currentPage = SurveyCollection.query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPage - 1),
        },function(result) {
          $scope.pageState.surveys = result._embedded.surveys;
          $scope.pageState.totalElements = result.page.totalElements;
        });
      };
      $scope.$on('refresh', function() {
        init();
      });
      init();
    });
