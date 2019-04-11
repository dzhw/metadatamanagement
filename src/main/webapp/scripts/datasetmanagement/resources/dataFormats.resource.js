'use strict';

angular.module('metadatamanagementApp')
  .factory('DataFormatsResource', function($resource) {
    return $resource('api/data-sets/data-formats');
  });
