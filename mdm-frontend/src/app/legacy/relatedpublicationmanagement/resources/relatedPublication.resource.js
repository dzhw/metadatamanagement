'use strict';
/* @author Daniel Katzberg */

angular.module('metadatamanagementApp')
  .factory('RelatedPublicationResource', ['$resource',  function($resource) {
    return $resource('api/related-publications/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  }]);

