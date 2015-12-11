'use strict';

angular.module('metadatamanagementApp')
    .factory('Survey', function($resource, DateUtils) {
      return $resource('api/surveys/:id', {}, {
        'query': {method: 'GET', isArray: true},
        'get': {
          method: 'GET',
          transformResponse: function(data) {
            data = angular.fromJson(data);
            data.fieldPeriod =
              DateUtils.convertLocaleDateFromServer(data.fieldPeriod);
            return data;
          }
        },
        'update': {
          method: 'PUT',
          transformRequest: function(data) {
            data.fieldPeriod =
              DateUtils.convertLocaleDateToServer(data.fieldPeriod);
            return angular.toJson(data);
          }
        },
        'create': {
          method: 'POST',
          transformRequest: function(data) {
            data.fieldPeriod =
              DateUtils.convertLocaleDateToServer(data.fieldPeriod);
            return angular.toJson(data);
          }
        }
      });
    });
