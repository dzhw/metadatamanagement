/* global Keycloak , window */
'use strict';

angular
  .module('metadatamanagementApp')
  .factory(
    'Auth',
    function Auth($q, LanguageService) {
      var keycloak = new Keycloak({
        realm: 'fdz-dzhw',
        url: 'http://localhost:8082/auth/',
        clientId: 'mdm-frontend'
      });
      keycloak.onAuthLogout = function() {
        console.log('Logout !!!');
      };
      return {
        init: function() {
          var deferred = $q.defer();
          keycloak.init({
            onLoad: 'check-sso',
            silentCheckSsoRedirectUri:
              window.location.origin + '/silent-check-sso.html',
            enableLogging: true
          }).then(function() {
            keycloak.createLogoutUrl({
              redirectUri: window.location.origin
            });
            console.log(keycloak);
            deferred.resolve(keycloak);
          }).catch(function(error) {
            deferred.reject(error);
          });
          return deferred;
        },

        login: function() {
          keycloak.login({
            redirectUri: window.location.origin +
              '/' + LanguageService.getCurrentInstantly() + '/search',
            locale: LanguageService.getCurrentInstantly()
          });
        },

        logout: function() {
          keycloak.logout({
            redirectUri: window.location.origin
          });
        },

        isAuthenticated: function() {
          return keycloak.authenticated;
        },

        getIdToken: function() {
          return keycloak.idTokenParsed;
        },

        getAccessToken: function() {
          return keycloak.tokenParsed;
        },

        getAccessTokenEncoded: function() {
          return keycloak.token;
        }
      };
    });
