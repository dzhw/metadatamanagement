'use strict';

angular.module('metadatamanagementApp').factory('MonitoringService',
  function($http) {
    return {
      checkHealth: function() {
        return $http.get('/management/health').then(function(response) {
          return response.data;
        });
      }
    };
  });
