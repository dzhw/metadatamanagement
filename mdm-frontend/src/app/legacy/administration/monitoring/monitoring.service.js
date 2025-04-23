'use strict';

angular.module('metadatamanagementApp').factory('MonitoringService', ['$http', 
  function($http) {
    return {
      checkHealth: function() {
        return $http.get('/management/health').then(function(response) {
          return response.data;
        });
      },
      checkDaraPidHealth: function() {
        return $http.get('/management/health/daraPid')
          .then(response => response.data.status === 'UP')
          .catch(response => response.data.status === 'UP');
      }
    };
  }]);

