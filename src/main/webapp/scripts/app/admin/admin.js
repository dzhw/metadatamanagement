'use strict';

angular.module('metadatamanagementApp').config(function($stateProvider) {
  $stateProvider.state('admin', {
    abstract: true,
    parent: 'site'
  });
});
