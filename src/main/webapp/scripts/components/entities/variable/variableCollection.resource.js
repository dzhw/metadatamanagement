'use strict';

angular.module('metadatamanagementApp')
    .factory('VariableCollection', function($resource) {
      return $resource('/api/variables',
        {}, {
        'query': {
          method: 'GET',
        }
      });
    });
