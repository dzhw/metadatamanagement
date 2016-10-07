'use strict';
/* @author Daniel Katzberg */

angular.module('metadatamanagementApp')
  .factory('RelatedPublicationResource', function($resource, $state) {
    return $resource('api/related-publications/:id', {
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
              $state.go('error');
            }
          }
        }
      },
      'save': {
        method: 'PUT'
      }
    });
  });
