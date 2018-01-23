/* Author: Daniel Katzberg */
'use strict';

/* Study Resource */
angular.module('metadatamanagementApp')
  .factory('StudyResource', function($resource, CleanJSObjectService) {
    return $resource('/api/studies/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'PUT',
        transformRequest: function(study) {
          var copy = angular.copy(study);
          CleanJSObjectService.deleteEmptyStrings(copy);
          CleanJSObjectService.removeEmptyJsonObjects(copy);
          return angular.toJson(copy);
        }
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });
