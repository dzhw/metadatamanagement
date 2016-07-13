'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetDetailController', ['$scope', 'entity',
    function($scope, entity) {
      $scope.dataSet = entity;
      console.log($scope.dataSet);
    }
  ]);
