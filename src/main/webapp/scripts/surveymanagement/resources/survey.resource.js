'use strict';

angular.module('metadatamanagementApp')
  .factory('SurveyResource', function($resource, CleanJSObjectService) {
    return $resource('/api/surveys/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        }
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
