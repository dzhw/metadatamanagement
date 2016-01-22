'use strict';

angular.module('metadatamanagementApp')
    .factory('Survey', function($resource, DateUtils) {
      return $resource('/api/surveys/:id',
        {id: '@id'}, {
        'get': {
          method: 'GET',
          params: {projection: 'complete'},
          transformResponse: function(data) {
            // data might be empty if 404
            if (data) {
              data = angular.fromJson(data);
              data.fieldPeriod.start =
                DateUtils.convertLocaleDateFromServer(data.fieldPeriod.start);
              data.fieldPeriod.end =
                DateUtils.convertLocaleDateFromServer(data.fieldPeriod.end);
              data.fdzProject = data.fdzProject.id;
              return data;
            }
          }
        },
        'save': {
          method: 'PUT',
          transformRequest: function(data) {
            data.fieldPeriod.start =
              DateUtils.convertLocaleDateToServer(data.fieldPeriod.start);
            data.fieldPeriod.end =
              DateUtils.convertLocaleDateToServer(data.fieldPeriod.end);
            data.fdzProject = '/api/fdz_projects/' +
              encodeURIComponent(data.fdzProject);
            return angular.toJson(data);
          }
        },
        'delete': {
          method: 'DELETE'
        }
      });
    });
