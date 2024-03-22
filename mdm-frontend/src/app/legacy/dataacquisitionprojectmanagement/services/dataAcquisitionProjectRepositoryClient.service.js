'use strict';

angular.module('metadatamanagementApp').factory('DataAcquisitionProjectRepositoryClient', ['$http',  function($http) {
    var fetchAll = function() {
      return $http({
        method: 'GET',
        url: '/api/data-acquisition-projects/search/findAll',
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
  
    var findByIdLikeOrderByIdAsc = function(id, page, limit, packageType) {
      return $http({
        method: 'GET',
        url: '/api/data-acquisition-projects/search/findByIdLikeOrderByIdAsc',
        params: {
          id: id,
          page: page,
          size: limit,
          type: packageType
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

    var findAssignedProjects = function(login) {
      return $http({
        method: 'GET',
        url: '/api/data-acquisition-projects/search/' +
          'findAllByConfigurationPublishersContains' +
          'OrConfigurationDataProvidersContainsAndShadowIsFalse',
        params: {
          login: login
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

    var findAssignedProjectsAsDataProvider = function(login) {
      return $http({
        method: 'GET',
        url: '/api/data-acquisition-projects/search/' +
          'findAllByConfigurationDataProvidersContainsAndShadowIsFalse',
        params: {
          login: login
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

    var findAssignedProjectsAsPublisher = function(login) {
      return $http({
        method: 'GET',
        url: '/api/data-acquisition-projects/search/' +
          'findAllByConfigurationPublishersContainsAndShadowIsFalse',
        params: {
          login: login
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
      findAssignedProjects: findAssignedProjects,
      findAssignedProjectsAsPublisher: findAssignedProjectsAsPublisher,
      findAssignedProjectsAsDataProvider: findAssignedProjectsAsDataProvider,
      findByIdLikeOrderByIdAsc: findByIdLikeOrderByIdAsc,
      fetchAll: fetchAll
    };
  }]);

