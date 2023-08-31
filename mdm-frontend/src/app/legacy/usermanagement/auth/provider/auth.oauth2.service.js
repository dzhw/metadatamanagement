'use strict';

angular
  .module('metadatamanagementApp')
  .factory('AuthServerProvider', ['$http', 'localStorageService', 'Base64', 
    function loginService($http, localStorageService, Base64) {
      return {
        login: function(credentials) {
          var data = 'username=' +
            encodeURIComponent(credentials.username) +
            '&password=' +
            encodeURIComponent(credentials.password) +
            '&grant_type=password&scope=read%20write&' +
            'client_secret=mySecretOAuthSecret&' +
            'client_id=metadatamanagementapp';
          return $http.post(
            'oauth/token',
            data, {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Accept': 'application/json',
                'Authorization': 'Basic ' +
                  Base64.encode('metadatamanagementapp' + ':' +
                    'metadatamanagementClient')
              }
            }).then(
            function(response) {
              localStorageService.set('token', response.data);
              return response;
            });
        },
        logout: function() {
          // logout from the server
          localStorageService.remove('token');
          $http.post('api/logout');
        },
        getToken: function() {
          return localStorageService.get('token');
        },
        deleteToken: function() {
          localStorageService.remove('token');
        },
        hasToken: function() {
          var token = this.getToken();
          //jscs:disable
          if (token) {
            return true;
          } else {
            return false;
          }
          //jscs:enable
        }
      };
    }]);

