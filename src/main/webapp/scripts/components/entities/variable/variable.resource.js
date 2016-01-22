'use strict';

angular.module('metadatamanagementApp')
  .factory('Variable', function($resource) {
    return $resource('api/variables/:id', {id: '@id'}, {
      'get': {
        method: 'GET',
        params:  {projection: 'complete'},
        transformResponse: function(data) {
          if (data) {
            data = angular.fromJson(data);
            data.fdzProject = data.fdzProject.id;
            data.survey = data.survey.id;
            return data;
          }
        }
      },
      'save': {
        method: 'PUT',
        transformRequest: function(data) {
          //convert id to uri as required by spring data rest
          data.fdzProject = '/api/fdz_projects/' + data.fdzProject;
          //convert id to uri as required by spring data rest
          if (data.survey) {
            data.survey = '/api/surveys/' + data.survey;
          }
          return angular.toJson(data);
        }
      },
      'delete': {
        method: 'DELETE',
      }
    });
  });
