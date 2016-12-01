'use strict';
angular.module('metadatamanagementApp').factory('authInterceptor', function(
    $rootScope, $q, $location, localStorageService) {
    return {
        // Add authorization token to headers
        request: function(config) {
            config.headers = config.headers || {};
            var token = localStorageService.get('token');
            //jscs:disable
            if (token) {
                config.headers.Authorization = 'Bearer ' + token.access_token;
            }
            //jscs:enable
            return config;
          }
      };
  });
