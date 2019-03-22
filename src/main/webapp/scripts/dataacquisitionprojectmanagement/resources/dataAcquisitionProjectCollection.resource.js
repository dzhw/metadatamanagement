'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectCollectionResource', function($resource) {
    return $resource('/api/data-acquisition-projects', {
      page: 0,
      sort: 'id,asc',
      shadow: false,
      projection: 'dataAcquisitionProjectStateSummary'
    }, {
      'get': {
        method: 'GET',
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
