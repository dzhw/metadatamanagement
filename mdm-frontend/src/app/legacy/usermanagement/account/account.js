'use strict';

angular.module('metadatamanagementApp').config([
  '$stateProvider',
function($stateProvider) {
  $stateProvider.state('account', {
    abstract: true,
    parent: 'site'
  });
}]);
