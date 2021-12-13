'use strict';

angular
  .module('metadatamanagementApp')
  .factory(
    'Auth',
    function Auth($rootScope, Principal, AuthServiceProvider, $q) {
      return {
        login: function() {
          return AuthServiceProvider.login();
        },
        logout: function(complete) {
          if (complete) {
            AuthServiceProvider.logout();
          }
          $rootScope.identity = {};
          // Reset state memory
          $rootScope.previousStateName = undefined;
          $rootScope.previousStateParams = undefined;
          $rootScope.$broadcast('user-logged-out');
        },
        authorize: function(code) {
          return AuthServiceProvider.authorize(code);
        },
        init: function() {
          var deferred = $q.defer();
          AuthServiceProvider.isLoggedIn().then(
            function(res) {
              if (res === 'sso') {
                AuthServiceProvider.login();
              } else {
                Principal.identity().then(function() {
                  $rootScope.identity = AuthServiceProvider.idTokenInfo();
                  deferred.resolve(res);
                });
              }
            },
            function() {
              deferred.resolve(false);
            }
          );
          return deferred.promise;
        }
      };
    });
