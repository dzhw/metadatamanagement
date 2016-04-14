'use strict';

angular.module('metadatamanagementApp')
    .factory('VariableCollection', function($resource) {
      return $resource('/api/variables',
        {projection: 'complete'}, {
        'query': {
          method: 'GET',
        }
      });
    });
