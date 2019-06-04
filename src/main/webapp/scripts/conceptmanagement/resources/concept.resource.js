/* Author: Daniel Katzberg */
'use strict';

/* Study Resource */
angular.module('metadatamanagementApp')
  .factory('ConceptResource', function($resource, CleanJSObjectService) {
    var baseUrl = '/api/concepts';
    return $resource(baseUrl + '/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'PUT',
        transformRequest: function(concept) {
          var copy = angular.copy(concept);
          CleanJSObjectService.deleteEmptyStrings(copy);
          CleanJSObjectService.removeEmptyJsonObjects(copy);
          return angular.toJson(copy);
        }
      },
      'delete': {
        method: 'DELETE'
      },
      'query': {
        url: baseUrl
      }
    });
  });
