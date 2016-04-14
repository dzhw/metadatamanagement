'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyDetailController', function($scope, $rootScope,
    $stateParams, entity) {
    $scope.survey = entity;
  });
