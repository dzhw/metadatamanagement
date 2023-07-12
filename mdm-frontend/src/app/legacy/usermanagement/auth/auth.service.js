'use strict';

angular
  .module('metadatamanagementApp')
  .factory(
    'Auth',
    function Auth($rootScope, $q, Principal,
      AuthServerProvider, AccountResource, RegisterResource, ActivateResource,
      PasswordResource, PasswordResetInitResource,
      PasswordResetFinishResource) {
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
          $rootScope.$broadcast('user-logged-out');
        },

        authorize: function(force) {
          return Principal
            .identity(force)
            .then(
              function(identity) {
                if (Principal.isAuthenticated()) {
                  $rootScope.identity = identity;
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
