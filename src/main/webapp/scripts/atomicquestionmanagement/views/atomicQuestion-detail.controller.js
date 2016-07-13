'use strict';

angular.module('metadatamanagementApp')
  .controller('AtomicQuestionDetailController', ['$scope', 'entity',
    function($scope, entity) {
      $scope.atomicQuestion = entity;
      console.log($scope.atomicQuestion);
    }
  ]);
