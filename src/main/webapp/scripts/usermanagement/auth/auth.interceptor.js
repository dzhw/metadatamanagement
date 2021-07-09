'use strict';
angular.module('metadatamanagementApp').factory('authInterceptor', function(
  Auth) {
  return {
    // Add authorization token to headers
    request: function(config) {
      config.headers = config.headers || {};
      var token = Auth.getAccessTokenEncoded();
      //jscs:disable
      if (token) {
        config.headers.Authorization = 'Bearer ' + token;
      }
      //jscs:enable
      return config;
    }
  };
});
