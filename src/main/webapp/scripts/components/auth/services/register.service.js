'use strict';

angular.module('metadatamanagementApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


