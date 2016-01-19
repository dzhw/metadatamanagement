'use strict';

angular.module('metadatamanagementApp')
  .factory('Variable', function($resource) {
    return $resource('api/variables/:id', {id: '@id'}, {
      'get': {
        method: 'GET',
        params:  {projection: 'complete'}
      },
      'update': {
        method: 'PUT',
      },
      'create': {
        method: 'POST',
        transformRequest: function(data) {
          //convert id to uri as required by spring data rest
          data.fdzProject = '/api/fdz_projects/' + data.fdzProject.id;
          //convert id to uri as required by spring data rest
          if (data.survey) {
            data.survey = '/api/surveys/' + data.survey.id;
          }
          return angular.toJson(data);
        }
      },
      'delete': {
        method: 'DELETE',
      },
      'findOneByFdzId': {
        method: 'GET',
        params: {projection: 'complete'},
        url: '/api/variables/search/findOneByFdzId',
      }
    });
  });
