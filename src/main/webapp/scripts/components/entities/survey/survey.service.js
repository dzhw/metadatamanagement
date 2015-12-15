'use strict';

angular.module('metadatamanagementApp')
    .factory('Survey', function($resource, DateUtils) {
      return $resource('api/surveys/:id', {}, {
        'query': {method: 'GET', isArray: true},
        'get': {
          method: 'GET',
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
        'update': {
          method: 'PUT',
          transformRequest: function(data) {
            data.fieldPeriod.start =
              DateUtils.convertLocaleDateToServer(data.fieldPeriod.start);
            data.fieldPeriod.end =
                DateUtils.convertLocaleDateToServer(data.fieldPeriod.end);
            return angular.toJson(data);
          }
        },
        'create': {
          method: 'POST',
          transformRequest: function(data) {
            data.fieldPeriod.start =
              DateUtils.convertLocaleDateToServer(data.fieldPeriod.start);
            data.fieldPeriod.end =
                DateUtils.convertLocaleDateToServer(data.fieldPeriod.end);
            return angular.toJson(data);
          }
        }
      });
    });
