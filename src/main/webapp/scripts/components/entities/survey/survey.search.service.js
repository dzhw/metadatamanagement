'use strict';

angular.module('metadatamanagementApp')
    .factory('SurveySearch', function ($resource) {
        return $resource('api/_search/surveys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
