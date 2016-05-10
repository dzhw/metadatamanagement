'use strict';

angular.module('metadatamanagementApp')
    .factory('SurveyResource', function($resource, DateUtils, $state) {
      return $resource('/api/surveys/:id',
        {id: '@id'}, {
        'get': {
          method: 'GET',
          params: {projection: 'complete'},
          interceptor: {
            responseError: function(response) {
              if (response.status === 404) {
                $state.go('error');
              }
            },
            transformResponse: function(data) {
              // data might be empty if 404
              if (data) {
                data = angular.fromJson(data);
                data.fieldPeriod.start =
                DateUtils.convertLocaleDateFromServer(data.fieldPeriod.start);
                data.fieldPeriod.end =
                DateUtils.convertLocaleDateFromServer(data.fieldPeriod.end);
                return data;
              }
            }
          },
        },
        'save': {
          method: 'PUT',
          transformRequest: function(data) {
            data.fieldPeriod.start =
              DateUtils.convertLocaleDateToServer(data.fieldPeriod.start);
            data.fieldPeriod.end =
              DateUtils.convertLocaleDateToServer(data.fieldPeriod.end);
            return angular.toJson(data);
          }
        },
        'delete': {
          method: 'DELETE'
        }
      });
    });
