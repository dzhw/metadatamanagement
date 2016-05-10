'use strict';

angular.module('metadatamanagementApp')
    .factory('VariableCollectionResource', function($resource) {
      return $resource('/api/variables',
        {projection: 'complete'}, {
        'query': {
          method: 'GET',
        }
      });
    });
