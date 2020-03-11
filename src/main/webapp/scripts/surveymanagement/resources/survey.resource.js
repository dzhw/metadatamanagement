'use strict';

angular.module('metadatamanagementApp')
  .factory('SurveyResource', function($resource, CleanJSObjectService) {
    return $resource('/api/surveys/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'PUT',
        transformRequest: function(survey) {
          var copy = angular.copy(survey);
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
