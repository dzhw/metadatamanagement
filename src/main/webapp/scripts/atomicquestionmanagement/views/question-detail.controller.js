'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController', ['$scope', 'entity',
    function($scope, entity) {
      $scope.question = entity;
    }
  ]);
