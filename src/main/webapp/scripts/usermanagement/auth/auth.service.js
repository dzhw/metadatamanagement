'use strict';

angular
  .module('metadatamanagementApp')
  .factory(
    'Auth',
    function Auth($rootScope, $state, $q, Principal,
      AuthServerProvider, AccountResource, RegisterResource, ActivateResource,
      PasswordResource, PasswordResetInitResource,
      PasswordResetFinishResource, LanguageService) {
      return {
        login: function(credentials, callback) {
          var cb = callback || angular.noop;
          var deferred = $q.defer();

          AuthServerProvider.login(credentials).then(function(data) {

            //retrieve the logged account information
            Principal.identity(true).then(function(identity) {
              deferred.resolve(data);
              $rootScope.identity = identity;
            });
            return cb();
          }).catch(function(err) {
              //this.logout();
              deferred.reject(err);
              return cb(err);
            }
            .bind(this));

          return deferred.promise;
        },

        logout: function() {
          AuthServerProvider.logout();
          $rootScope.identity = {};
          Principal.authenticate(null);
          // Reset state memory
          $rootScope.previousStateName = undefined;
          $rootScope.previousStateParams = undefined;
        },

        authorize: function(force) {
          return Principal
            .identity(force)
            .then(
              function(identity) {
                var isAuthenticated = Principal.isAuthenticated();

                if (isAuthenticated) {
                  $rootScope.identity = identity;
                }
                // an authenticated user can't access to login and
                // register pages
                if (isAuthenticated &&
                  $rootScope.toState.parent === 'account' &&
                  ($rootScope.toState.name === 'login' ||
                    $rootScope.toState.name === 'register')) {
                  $state.go('search', {
                    lang: LanguageService.getCurrentInstantly()
                  });
                }

                if ($rootScope.toState.data.authorities &&
                  $rootScope.toState.data.authorities.length > 0 &&
                  !Principal.hasAnyAuthority(
                    $rootScope.toState.data.authorities)) {
                  if (!isAuthenticated) {
                    // user is not authenticated. stow the state
                    // they wanted before you
                    // send them to the signin state, so you can
                    // return them when you're done
                    $rootScope.previousStateName =
                      $rootScope.toState.name;
                    $rootScope.previousStateParams =
                      $rootScope.toStateParams;
                    // now, send them to the signin state so they
                    // can log in
                    $state.go('login', {
                      lang: LanguageService.getCurrentInstantly()
                    });
                  }
                }
              });
        },
        createAccount: function(account, callback) {
          var cb = callback || angular.noop;

          return RegisterResource.save(account, function() {
            return cb(account);
          }, function(err) {
            this.logout();
            return cb(err);
          }.bind(this)).$promise;
        },

        updateAccount: function(account, callback) {
          var cb = callback || angular.noop;

          return AccountResource.save(account, function() {
            return cb(account);
          }, function(err) {
            return cb(err);
          }.bind(this)).$promise;
        },

        activateAccount: function(key, callback) {
          var cb = callback || angular.noop;

          return ActivateResource.get(key, function(response) {
            return cb(response);
          }, function(err) {
            return cb(err);
          }.bind(this)).$promise;
        },

        changePassword: function(newPassword, callback) {
          var cb = callback || angular.noop;

          return PasswordResource.save(newPassword, function() {
            return cb();
          }, function(err) {
            return cb(err);
          }).$promise;
        },

        resetPasswordInit: function(mail, callback) {
          var cb = callback || angular.noop;

          return PasswordResetInitResource.save(mail, function() {
            return cb();
          }, function(err) {
            return cb(err);
          }).$promise;
        },

        resetPasswordFinish: function(keyAndPassword, callback) {
          var cb = callback || angular.noop;

          return PasswordResetFinishResource.save(keyAndPassword, function() {
            return cb();
          }, function(err) {
            return cb(err);
          }).$promise;
        }
      };
    });
