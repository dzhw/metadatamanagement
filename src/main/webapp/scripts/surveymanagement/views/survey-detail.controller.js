'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController', ['$scope', 'entity',
    function($scope, entity) {
      $scope.survey = entity;
      console.log($scope.survey);
    }
  ]);
