'use strict';

angular.module('metadatamanagementApp')
  .factory('DataFormatsResource', ['$resource',  function($resource) {
    return $resource('api/data-sets/data-formats');
  }]);

