'use strict';

angular.module('metadatamanagementApp').factory('MonitoringService', ['$http', 
  function($http) {
    return {
      checkHealth: function() {
        return $http.get('/management/health').then(function(response) {
          return response.data;
        });
      }
    };
  }]);

