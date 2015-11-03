'use strict';

angular.module('metadatamanagementApp')
    .factory('VariableSearch', function ($resource) {
        return $resource('api/_search/variables/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
