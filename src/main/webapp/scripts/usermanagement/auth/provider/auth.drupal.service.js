'use strict';

angular
  .module('metadatamanagementApp')
  .factory(
    'AuthServiceProvider',
    function loginService($http, localStorageService, $window,
                          $q, AuthProperties) {
      var config = {
        clientId: AuthProperties.clientId,
        clientSecret: AuthProperties.clientSecret,
        redirectUri: 'http://localhost:3000',
        authUrl: AuthProperties.issuer + '/oauth/authorize',
        tokenUrl: AuthProperties.issuer + '/oauth/token',
        userInfo: AuthProperties.issuer + '/oauth/userinfo',
        logout: AuthProperties.issuer + '/user/logout',
        scope: 'openid email profile role_admin ' +
          'role_data_provider role_publisher role_release_manager'
      };
      var decodeToken = function(token) {
        if (token) {
          var base64Url = token.split('.')[1];
          var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
          return JSON.parse(($window.atob(base64)));
        }
        return false;
      };
      return {
        login: function() {
          var url =
            config.authUrl +
            '?response_type=code' +
            '&client_id=' +
            encodeURIComponent(config.clientId) +
            '&redirect_uri=' +
            encodeURIComponent(config.redirectUri) +
            '&state=' + 'auth' +
            '&scope=' + encodeURIComponent(config.scope);

          $window.location.href = url;
        },
        isLoggedInSso: function() {
          var deferred = $q.defer();
          var url = AuthProperties.issuer + '/user/login?_format=json';
          $http.get(
            url,
            {
              headers: {
                'Content-type': 'application/json'
              }
            }
          ).then(
            function() {
              deferred.resolve();
            },
            function(error) {
              deferred.reject(error);
            }
          );
          return deferred.promise;
        },
        isLoggedIn: function() {
          var deferred = $q.defer();

          if (!this.hasToken()) {
            this.isLoggedInSso().then(
              function() {
                deferred.resolve('sso');
              },
              function() {
                deferred.reject(false);
              });
            return deferred.promise;

          } else {
            deferred.reject(false);
          }
          return deferred.promise;
        },
        authorize: function(code) {
          var deferred = $q.defer();

          if (code) {
            var data =
              'code=' + encodeURIComponent(code) +
              '&grant_type=authorization_code' +
              '&redirect_uri=' + encodeURIComponent(config.redirectUri) +
              '&client_id=' + encodeURIComponent(config.clientId);

            $http.post(
              config.tokenUrl,
              data, {
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded',
                  'Accept': 'application/json'
                }
              }).then(
              function(response) {
                localStorageService.set('tokens', response.data);
                deferred.resolve();
              },
              function(error) {
                deferred.reject(error);
              });
          }
          return deferred.promise;
        },
        logout: function() {
          $http.post(config.logout).then(
            function(response) {
              //@todo: call logout?
              localStorageService.remove('tokens');
              return response;
            },
            function(error) {
              console.log(error);
            });
        },
        getUserInfo: function() {
          var deferred = $q.defer();
          if (this.hasToken()) {
            // request userInfo from dpl
            $http.get(
              config.userInfo,
              {
                headers: {
                  'Accept': 'application/json',
                  'Authorization': 'Bearer ' + this.getToken().access_token
                }
              }
            ).then(
              function(response) {
                deferred.resolve(response.data);
              },
              function(error) {
                deferred.reject(error);
              }
            );
          } else {
            deferred.reject();
          }
          return deferred.promise;
        },
        accessTokenInfo: function() {
          return decodeToken(this.getToken().access_token);
        },
        idTokenInfo: function() {
          return decodeToken(this.getToken().id_token);
        },
        getToken: function() {
          return localStorageService.get('tokens') || false;
        },
        refreshToken: function() {
          console.log('refresh token');
          var deferred = $q.defer();
          if (localStorageService.get('tokens')) {
            var data =
              'grant_type=refresh_token' +
              '&refresh_token=' +
              localStorageService.get('tokens').refresh_token +
              '&client_id=' + encodeURIComponent(config.clientId) +
              '&client_secret=' + encodeURIComponent(config.clientSecret);
            $http.post(
              config.tokenUrl,
              data, {
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded',
                  'Accept': 'application/json'
                }
              }).then(
              function(response) {
                localStorageService.set('tokens', response.data);
                deferred.resolve();
              },
              function(error) {
                deferred.reject(error);
              });
          } else {
            deferred.reject();
          }

          return deferred.promise;
        },
        deleteToken: function() {
          localStorageService.remove('tokens');
        },
        hasToken: function() {
          return !!this.getToken().access_token;
        }
      };
    });
