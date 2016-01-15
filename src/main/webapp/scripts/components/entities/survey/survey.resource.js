'use strict';

angular.module('metadatamanagementApp')
    .factory('Survey', function($resource, DateUtils) {
      return $resource('/api/surveys/:id',
        {id: '@id'}, {
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
            //convert id to uri as required by spring data rest
            data.fdzProject = '/api/fdz_projects/' + data.fdzProject.id;
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
            //convert id to uri as required by spring data rest
            data.fdzProject = '/api/fdz_projects/' + data.fdzProject.id;
            return angular.toJson(data);
          }
        },
        'delete': {
          method: 'DELETE',
        },
        'findOneByFdzId': {
          method: 'GET',
          params: {projection: 'complete'},
          url: '/api/surveys/search/findOneByFdzId',
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
        }
      });
    });
