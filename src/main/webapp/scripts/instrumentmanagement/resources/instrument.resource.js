'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('InstrumentResource', function($resource, $state, LanguageService) {
    return $resource('/api/instruments/:id', {
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
