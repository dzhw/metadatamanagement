'use strict';

angular.module('metadatamanagementApp').factory(
  'Principal',
  function Principal($q, AuthServiceProvider, $rootScope, $sessionStorage,
                     WelcomeDialogService, $state, LanguageService, $injector
                     //, $http
                     ) {

    if (AuthServiceProvider.hasToken()) {
      $rootScope.identity = AuthServiceProvider.idTokenInfo();
    }
    var uiLoggedIn = $sessionStorage.get('uiLoginState') || false;

    var displayWelcomeDialog = function() {
      const tokenInfo = AuthServiceProvider.idTokenInfo();
      return !tokenInfo.welcome_dialog_deactivated;
      // return _identity &&
      //   _.indexOf(identity.authorities, 'ROLE_DATA_PROVIDER') !== -1 &&
      //   !identity.welcomeDialogDeactivated;
    };


    /**
     * This function decativates the welcome dialog if the user has not
     * set this to false before and the return value of the dialog is false
     * @param {bool} hideDialog should Dialog be hid next time
     * @returns Http-Respone
     */
    var deactivateWelcomeDialogIDP = function(hideDialog /* bool */) {
      if (hideDialog && 
        !AuthServiceProvider.idTokenInfo().welcome_dialog_deactivated) {
          //return $http.patch("api/users/deactivatedWelcomeDialog?deactivatedWelcomeDialog=true");
      }
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
        console.log('switch', redirect, AuthServiceProvider.hasToken());
        if (AuthServiceProvider.hasToken()) {
          uiLoggedIn = !uiLoggedIn; // why?
          $sessionStorage.put('uiLoginState', uiLoggedIn);

          if (!uiLoggedIn) {
            var Auth = $injector.get('Auth');
            Auth.logout(true);
            // the same thing is done in auth service
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
          AuthServiceProvider.login().then(function() {
            $sessionStorage.put('uiLoginState', true);
          });
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
                console.log('after welcome dialog', hideWelcomeDialog);
                
                deactivateWelcomeDialogIDP(hideWelcomeDialog);
                //   .then((res) => console.log(res))
                //   .error((err) => console.log(err));
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
