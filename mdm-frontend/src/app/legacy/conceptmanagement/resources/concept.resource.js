/* Author: Daniel Katzberg */
'use strict';

/* Concept Resource */
angular.module('metadatamanagementApp')
  .factory('ConceptResource', ['$resource', 'CleanJSObjectService',  function($resource, CleanJSObjectService) {
    return $resource('/api/concepts/:id', {
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
      }
    });
  }]);

