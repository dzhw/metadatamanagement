'use strict';

angular.module('metadatamanagementApp').factory(
  'DataAcquisitionProjectRepositoryClient', function($http) {
    var findByIdLikeOrderByIdAsc = function(id) {
      return $http({
        method: 'GET',
        url: '/api/data-acquisition-projects/search/findByIdLikeOrderByIdAsc',
        params: {
          id: id,
          projection: 'complete'
        },
        transformResponse: function(data) {
          var response = angular.fromJson(data);
          if (response._embedded &&
              response._embedded.dataAcquisitionProjects) {
            return response._embedded.dataAcquisitionProjects;
          }
          return response;
        }
      });
    };

    return {
      findByIdLikeOrderByIdAsc: findByIdLikeOrderByIdAsc
    };
  });
