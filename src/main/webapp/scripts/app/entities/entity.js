'use strict';

angular.module('metadatamanagementApp').config(function($stateProvider) {
  $stateProvider.state('entity', {
    abstract: true,
    parent: 'site'
  });
});
