'use strict';

angular.module('metadatamanagementApp')
  .factory('SurveyResource', function($resource, $state, LanguageService) {
    return $resource('/api/surveys/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        },
        interceptor: {
          responseError: function(response) {
            if (response.status === 404) {
              $state.go('error', {
                lang: LanguageService.getCurrentInstantly()
              });
            }
          }
        },
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });
