'use strict';

angular.module('metadatamanagementApp')
    .factory('Variable', function ($resource, DateUtils) {
        return $resource('api/variables/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
