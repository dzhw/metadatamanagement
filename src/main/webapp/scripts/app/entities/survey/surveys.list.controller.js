'use strict';

angular.module('metadatamanagementApp')
    .controller('SurveysListController', function($scope, SurveyCollection) {
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
        SurveyCollection.query({dataAcquisitionProjectId:
          $scope.params.dataAcquisitionProjectId,
          page: ($scope.pageState.currentPage - 1),
        }, function(result) {
          $scope.pageState.promiseParam = true;
          $scope.pageState.surveys = result._embedded.surveys;
          $scope.pageState.bigTotalItems = result.page.totalElements;
        });
      };
      $scope.$on('refresh', function() {
        init();
      });
      init();
    });
