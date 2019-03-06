'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectResource', function($resource, $rootScope) {
    var rootPath = '/api/data-acquisition-projects';
    return $resource(rootPath + '/:id', {
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
        interceptor: {
          response: function(response) {
            if (response.status === 200) {
              $rootScope.$broadcast('project-saved');
            }
          }
        }
      },
      'delete': {
        method: 'DELETE',
        interceptor: {
          response: function(response) {
            if (response.status === 200) {
              $rootScope.$broadcast('project-deleted');
            }
          }
        }
      },
      'statusOverview': {
        url: rootPath,
        method: 'GET',
        params: {
          page: 0,
          sort: 'id',
          dir: 'asc',
          projection: 'dataAcquisitionProjectStateSummary'
        },
        transformResponse: function(response) {
          var data = angular.fromJson(response);
          if (data._embedded) {
            return {
              page: data.page,
              data: data._embedded.dataAcquisitionProjects
            };
          } else {
            return data;
          }
        }
      }
    });
  });
