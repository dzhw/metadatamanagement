'use strict';
angular.module('metadatamanagementApp').factory('authInterceptor', ['localStorageService',  function(
  localStorageService) {
  return {
    // Add authorization token to headers to all API requests
    request: function(config) {
      config.headers = config.headers || {};
      var token = localStorageService.get('token');
      //jscs:disable
      if (token && (config.url.indexOf('/api/') === 0 ||
        config.url.indexOf('api/') === 0 ||
        config.url.indexOf('/management/') === 0 ||
        config.url.indexOf('management/') === 0)) {
        config.headers.Authorization = 'Bearer ' + token.access_token;
      }
      //jscs:enable
      return config;
    }
  };
}]);

