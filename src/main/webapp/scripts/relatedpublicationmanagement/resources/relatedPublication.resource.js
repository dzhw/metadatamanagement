'use strict';
/* @author Daniel Katzberg */

angular.module('metadatamanagementApp')
  .factory('RelatedPublicationResource', function($resource) {
    return $resource('api/related-publications/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        }
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });
