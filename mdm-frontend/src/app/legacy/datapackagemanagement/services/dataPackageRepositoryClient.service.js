'use strict';

angular.module('metadatamanagementApp').factory('DataPackageRepositoryClient', ['$http', 
  function($http) {
    var findByDataAcquisitionProjectId = function(projectId) {
      return $http({
        method: 'GET',
        url: '/api/data-packages/search/findByDataAcquisitionProjectId',
        params: {
          dataAcquisitionProjectId: projectId,
          projection: 'id-and-version'
        },
        transformResponse: function(data) {
          var response = angular.fromJson(data);
          if (response._embedded && response._embedded.dataPackages) {
            return response._embedded.dataPackages;
          }
          return response;
        }
      });
    };

    return {
      findByDataAcquisitionProjectId: findByDataAcquisitionProjectId
    };
  }]);

