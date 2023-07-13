/* globals _ */
'use strict';

angular.module('metadatamanagementApp').factory('Principal', ['$q', 'AccountResource', 'AuthServerProvider', '$rootScope', 'WelcomeDialogService', 
  function Principal($q, AccountResource, AuthServerProvider, $rootScope,
                     WelcomeDialogService) {
    var _identity;
    var _authenticated = false;

    var displayWelcomeDialog = function(identity) {
      return _.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1 &&
        !identity.welcomeDialogDeactivated;
    };

    return {
      isIdentityResolved: function() {
        return angular.isDefined(_identity);
      },
      isAuthenticated: function() {
        return _authenticated;
      },
      hasAuthority: function(authority) {
        if (!_authenticated || !_identity || !_identity.authorities) {
          return false;
        }

        return (_identity.authorities.indexOf(authority) !== -1);
      },
      hasAnyAuthority: function(authorities) {
        if (!_authenticated || !_identity || !_identity.authorities) {
          return false;
        }

        for (var i = 0; i < authorities.length; i++) {
          if (_identity.authorities.indexOf(authorities[i]) !== -1) {
            return true;
          }
        }

        return false;
      },
      authenticate: function(identity) {
        _identity = identity;
        _authenticated = identity !== null;
      },
      identity: function(force) {
        var deferred = $q.defer();

        if (force === true) {
          _identity = undefined;
        }

        // check and see if we have retrieved the identity data from the
        // server.
        // if we have, reuse it by immediately resolving
        if (angular.isDefined(_identity)) {
          deferred.resolve(_identity);

          return deferred.promise;
        }

        // retrieve the identity data from the server, update the
        // identity object, and then resolve.
        if (AuthServerProvider.hasToken()) {
          $rootScope.$broadcast('start-ignoring-401');
          AccountResource.get().$promise.then(function(account) {
            $rootScope.$broadcast('stop-ignoring-401');
            _identity = account.data;
            _authenticated = true;
            if (displayWelcomeDialog(_identity)) {
              WelcomeDialogService.display(_identity.login)
                .then(function(hideWelcomeDialog) {
                  if (hideWelcomeDialog) {
                    _identity.welcomeDialogDeactivated = true;
                    AccountResource.save(_identity);
                  }
                });
            }
            return deferred.resolve(_identity);
          }).catch(function() {
            $rootScope.$broadcast('stop-ignoring-401');
            AuthServerProvider.deleteToken();
            _identity = null;
            _authenticated = false;
            deferred.resolve(_identity);
          });
        } else {
          _identity = null;
          _authenticated = false;
          deferred.resolve(_identity);
        }
        return deferred.promise;
      },
      loginName: function() {
        return _identity && _identity.login;
      }
    };
  }]);

