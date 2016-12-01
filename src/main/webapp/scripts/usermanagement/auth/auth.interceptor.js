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
}).factory(
  'authExpiredInterceptor',
  function($rootScope, $q, $injector, localStorageService) {
    return {
      responseError: function(response) {
        // token has expired
        if (response.status === 401 &&
          (response.data.error === 'invalid_token' ||
            response.data.error === 'Unauthorized')) {
          localStorageService.remove('token');
          var Principal = $injector.get('Principal');
          if (Principal.isAuthenticated()) {
            var Auth = $injector.get('Auth');
            Auth.authorize(true);
          }
        }
        return $q.reject(response);
      }
    };
  });
