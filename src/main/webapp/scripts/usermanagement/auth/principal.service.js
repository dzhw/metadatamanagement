/* globals _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'Principal',
  function Principal($q, AuthServiceProvider, $rootScope,
                     WelcomeDialogService) {
    var _identity;
    var _authenticated = false;

    //@todo: remove init _identity
    if (!$rootScope.identity && AuthServiceProvider.hasToken()) {
      var accessInfo = AuthServiceProvider.accessTokenInfo();
      var idInfo = AuthServiceProvider.idTokenInfo();
      _authenticated = true;
      _identity = {
        activated: true,
        authorities: accessInfo.scope ? accessInfo.scope.map(function(e) {
          return e.toUpperCase();
        }) : [],
        email: idInfo.email,
        langKey: idInfo.local,
        login: idInfo.preferred_username,
        welcomeDialogDeactivated: false
      };
      $rootScope.identity = _identity;
    }

    //@todo: save welcome dialog state in dpl
    var displayWelcomeDialog = function(identity) {
      return _identity &&
        _.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1 &&
        !identity.welcomeDialogDeactivated;
    };

    return {
      isIdentityResolved: function() {
        return angular.isDefined(_identity);
      },
      isAuthenticated: function() {
        return AuthServiceProvider.hasToken();
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
      identity: function() {
        var deferred = $q.defer();

        if (displayWelcomeDialog(_identity)) {
          WelcomeDialogService.display(_identity.login)
            .then(function(hideWelcomeDialog) {
              if (hideWelcomeDialog) {
                _identity.welcomeDialogDeactivated = true;
                //@todo: save state in dpl user profile
              }
            });
        }

        deferred.resolve(_identity);
        //deferred.resolve(AuthServiceProvider.idTokenInfo());
        return deferred.promise;
      },
      loginName: function() {
        return AuthServiceProvider.hasToken() &&
          AuthServiceProvider.idTokenInfo().preferred_username;
      }
    };
  });
