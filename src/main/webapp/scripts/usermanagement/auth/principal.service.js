'use strict';

angular.module('metadatamanagementApp').factory(
  'Principal',
  function Principal($q, AuthServiceProvider, $rootScope,
                     WelcomeDialogService) {

    if (AuthServiceProvider.hasToken()) {
      $rootScope.identity = AuthServiceProvider.idTokenInfo();
    }

    //@todo: save welcome dialog state in dpl
    var displayWelcomeDialog = function() {
      /*return _identity &&
        _.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1 &&
        !identity.welcomeDialogDeactivated;*/
    };

    return {
      isAuthenticated: function() {
        return AuthServiceProvider.hasToken();
      },
      hasAuthority: function(authority) {
        if (!AuthServiceProvider.hasToken()) {
          return false;
        }
        return (AuthServiceProvider.accessTokenInfo().scope &&
          AuthServiceProvider.accessTokenInfo().scope.
          indexOf(authority.toLowerCase()) !== -1);
      },
      hasAnyAuthority: function(authorities) {
        if (!AuthServiceProvider.hasToken()) {
          return false;
        }
        for (var i = 0; i < authorities.length; i++) {
          if (AuthServiceProvider.accessTokenInfo().scope &&
            AuthServiceProvider.accessTokenInfo().scope.
            indexOf(authorities[i].toLowerCase()) !== -1) {
            return true;
          }
        }
        return false;
      },
      identity: function() {
        var deferred = $q.defer();

        if (displayWelcomeDialog()) {
          WelcomeDialogService.display(
            AuthServiceProvider.idTokenInfo().preferred_username)
            .then(function(hideWelcomeDialog) {
              if (hideWelcomeDialog) {
                //_identity.welcomeDialogDeactivated = true;
                //@todo: save state in dpl user profile
              }
            });
        }
        $rootScope.indentity = AuthServiceProvider.idTokenInfo();
        deferred.resolve(AuthServiceProvider.idTokenInfo());
        return deferred.promise;
      },
      loginName: function() {
        return AuthServiceProvider.hasToken() &&
          AuthServiceProvider.idTokenInfo().preferred_username;
      }
    };
  });
