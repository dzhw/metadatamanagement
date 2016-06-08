'use strict';

angular.module('metadatamanagementApp').factory('MonitoringService',
  function($rootScope, $http) {
    return {
      getMetrics: function() {
        return $http.get('/management/metrics').then(function(response) {
          return response.data;
        });
      },

      checkHealth: function() {
        return $http.get('/management/health').then(function(response) {
          return response.data;
        });
      },

      threadDump: function() {
        return $http.get('/management/dump').then(function(response) {
          return response.data;
        });
      }
    };
  });
