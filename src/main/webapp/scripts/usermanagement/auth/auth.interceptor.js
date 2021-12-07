'use strict';
angular.module('metadatamanagementApp').factory('authInterceptor', function(
  localStorageService, $injector, $q) {
  return {
    // Add authorization token to headers to all API requests
    request: function(config) {
      config.headers = config.headers || {};
      //jscs:disable
      if (localStorageService.get('tokens') && (config.url.indexOf('/api/') === 0 ||
        config.url.indexOf('api/') === 0 ||
        config.url.indexOf('/management/') === 0 ||
        config.url.indexOf('management/') === 0)) {

        var AuthServiceProvider = $injector.get('AuthServiceProvider');
        // check expire timestamp if token is valid for more than one minute
        if (AuthServiceProvider.hasToken() && AuthServiceProvider.accessTokenInfo().exp < new Date(Date.now() - 60000) / 1000) {
          return AuthServiceProvider.refreshToken().then(function() {
            config.headers.Authorization = 'Bearer ' + localStorageService.get('tokens').access_token;
            return config;
          });
        } else {
          config.headers.Authorization = 'Bearer ' + localStorageService.get('tokens').access_token;
          return config;
        }

      } else {
        return config;
      }
      //jscs:enable
    },
    responseError: function(response) {
      if (response.status === 401 &&
        (response.config.url.indexOf('/api/') === 0 ||
        response.config.url.indexOf('api/') === 0 ||
        response.config.url.indexOf('/management/') === 0 ||
        response.config.url.indexOf('management/') === 0)) {

        var AuthServiceProvider = $injector.get('AuthServiceProvider');
        AuthServiceProvider.logout();

        return response;
      } else {
        return $q.reject(response);
      }

    }
  };
});
