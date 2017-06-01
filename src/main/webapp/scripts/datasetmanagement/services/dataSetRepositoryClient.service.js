'use strict';

angular.module('metadatamanagementApp').factory('DataSetRepositoryClient',
  function($http) {
    var findByDataAcquisitionProjectId = function(projectId) {
      return $http({
        method: 'GET',
        url: '/api/data-sets/search/findByDataAcquisitionProjectId',
        params: {
          dataAcquisitionProjectId: projectId,
          projection: 'id-and-version'
        },
        transformResponse: function(data) {
          var response = angular.fromJson(data);
          if (response._embedded && response._embedded.dataSets) {
            return response._embedded.dataSets;
          }
          return response;
        }
      });
    };

    return {
      findByDataAcquisitionProjectId: findByDataAcquisitionProjectId
    };
  });
