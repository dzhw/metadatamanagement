'use strict';

angular.module('metadatamanagementApp').factory(
  'Principal',
  function Principal($q, AuthServiceProvider, $rootScope, localStorageService,
                     WelcomeDialogService, $state, LanguageService) {

    if (AuthServiceProvider.hasToken()) {
      $rootScope.identity = AuthServiceProvider.idTokenInfo();
    }
    var uiLoggedIn = localStorageService.get('uilstate') || false;

    //@todo: save welcome dialog state in dpl
    var displayWelcomeDialog = function() {
      /*return _identity &&
        _.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1 &&
        !identity.welcomeDialogDeactivated;*/
    };

    return {
      isUiLoggedIn: function() {
        return uiLoggedIn;
      },
      isLocalLoggedIn: function() {
        return AuthServiceProvider.hasToken();
      },
      isAuthenticated: function() {
        return uiLoggedIn && AuthServiceProvider.hasToken();
      },
      switchMode: function(redirect) {
        if (AuthServiceProvider.hasToken()) {
          uiLoggedIn = !uiLoggedIn;
          localStorageService.set('uilstate', uiLoggedIn);

          if (!uiLoggedIn) {
            $rootScope.identity = {};
            $rootScope.previousStateName = undefined;
            $rootScope.previousStateParams = undefined;
            $rootScope.$broadcast('user-logged-out');
          } else {
            $rootScope.identity = AuthServiceProvider.idTokenInfo();
          }

          if (redirect !== false) {
            $state.go('start', {
              lang: LanguageService.getCurrentInstantly()
            }, {
              reload: true
            });
          }
        } else {
          localStorageService.set('uilstate', false);
          AuthServiceProvider.login();
        }
      },
      hasAuthority: function(authority) {
        return (uiLoggedIn && AuthServiceProvider.hasToken() &&
          AuthServiceProvider.accessTokenInfo().scope &&
          AuthServiceProvider.accessTokenInfo().
          scope.indexOf(authority.toLowerCase()) !== -1);
      },
      hasAnyAuthority: function(authorities) {
        if (!uiLoggedIn || !AuthServiceProvider.hasToken()) {
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

        if (!uiLoggedIn) {
          deferred.reject();
        }
        if (uiLoggedIn && AuthServiceProvider.hasToken()) {
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
        }
        return deferred.promise;
      },
      loginName: function() {
        return uiLoggedIn && AuthServiceProvider.hasToken() &&
          AuthServiceProvider.idTokenInfo().preferred_username;
      }
    };
  });
