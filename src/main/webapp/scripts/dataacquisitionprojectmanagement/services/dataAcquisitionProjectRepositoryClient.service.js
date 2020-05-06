'use strict';

angular.module('metadatamanagementApp').factory(
  'DataAcquisitionProjectRepositoryClient', function($http) {
    var findByIdLikeOrderByIdAsc = function(id) {
      return $http({
        method: 'GET',
        url: '/api/data-acquisition-projects/findByIdLikeOrderByIdAsc',
        params: {
          id: id
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
      findByIdLikeOrderByIdAsc: findByIdLikeOrderByIdAsc
    };
  });
