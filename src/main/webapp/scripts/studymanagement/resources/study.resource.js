/* Author: Daniel Katzberg */
'use strict';

/* Study Resource */
angular.module('metadatamanagementApp')
  .factory('StudyResource', function($resource, $state, LanguageService) {
    return $resource('/api/studies/:id', {
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
