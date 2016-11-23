'use strict';

angular.module('metadatamanagementApp')
  .factory('DataSetResource', function($resource, $state, LanguageService) {
    return $resource('api/data-sets/:id', {
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
        }
      },
      'save': {
        method: 'PUT'
      }
    });
  });
