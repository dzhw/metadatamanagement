'use strict';

angular.module('metadatamanagementApp').config([
  '$stateProvider',
function($stateProvider) {
  $stateProvider.state('admin', {
    abstract: true,
    parent: 'site'
  });
}]);
