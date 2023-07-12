/* global moment */
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
          // convert to LocalDate (without timezone issues)
          if (copy.fieldPeriod.start) {
            copy.fieldPeriod.start =
              moment(copy.fieldPeriod.start).format('YYYY-MM-DD');
          }
          if (copy.fieldPeriod.end) {
            copy.fieldPeriod.end =
              moment(copy.fieldPeriod.end).format('YYYY-MM-DD');
          }
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
