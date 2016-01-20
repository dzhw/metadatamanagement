'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController',
    function($scope, entity) {
      $scope.variable = entity;
    });
