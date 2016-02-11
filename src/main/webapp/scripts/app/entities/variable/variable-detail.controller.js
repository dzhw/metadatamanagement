'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController',
    function($scope, entity, ShareEntity) {
      $scope.variable = entity;

      //Put the Entity to the a Service for set the entity ready for the charts
      //controller
      ShareEntity.setEntity(entity);
    });
