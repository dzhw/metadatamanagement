'use strict';

angular.module('metadatamanagementApp').factory(
  'Principal',
  function Principal($q, AuthServiceProvider, $rootScope,
                     WelcomeDialogService, $state,
                     LanguageService, $http) {

    if (AuthServiceProvider.hasToken()) {
      $rootScope.identity = AuthServiceProvider.idTokenInfo();
    }
    // var uiLoggedIn = $sessionStorage.get('uiLoginState') || false;

    var displayWelcomeDialog = function() {
      var tokenInfo = AuthServiceProvider.idTokenInfo();
      return !tokenInfo.welcome_dialog_deactivated;
    };

    var deactivateWelcomeDialogIDP = function(hideDialog) {
      if (hideDialog && !AuthServiceProvider.idTokenInfo().welcome_dialog_deactivated) {
        var url = 'api/users/deactivatedWelcomeDialog?deactivatedWelcomeDialog=true';
        return $http.patch(url);
      }
    };

    return {
      // isUiLoggedIn: function() {
      //   return uiLoggedIn;
      // },
      isLocalLoggedIn: function() {
        return AuthServiceProvider.hasToken();
      },
      isAuthenticated: function() {
        return /*uiLoggedIn && */ AuthServiceProvider.hasToken();
      },
      // _switchMode: function(redirect) {
      //   console.log('switch', redirect, AuthServiceProvider.hasToken());
      //   if (AuthServiceProvider.hasToken()) {
      //     uiLoggedIn = !uiLoggedIn;
      //     $sessionStorage.put('uiLoginState', uiLoggedIn);

      //     if (!uiLoggedIn) {
      //       AuthServiceProvider.logout();
      //       // var Auth = $injector.get('Auth');
      //       // Auth.logout(true);
      //       // the same thing is done in auth service
      //       $rootScope.identity = {};
      //       $rootScope.previousStateName = undefined;
      //       $rootScope.previousStateParams = undefined;
      //       $rootScope.$broadcast('user-logged-out');
      //     } else {
      //       $rootScope.identity = AuthServiceProvider.idTokenInfo();
      //     }

      //     if (redirect !== false) {
      //       $state.go('start', {
      //         lang: LanguageService.getCurrentInstantly()
      //       }, {
      //         reload: true
      //       });
      //     }
      //   } else {
      //     AuthServiceProvider.login().then(function() {
      //       $sessionStorage.put('uiLoginState', true);
      //     });
      //   }
      // },
      /**
       * Switch from logged in to logged out and vice versa
       *
       * @param {any} redirect
       */
      switchMode: function(redirect) {
        console.log('switch', redirect, AuthServiceProvider.hasToken());
        if (AuthServiceProvider.hasToken()) {
          // if token is present user is logged in so we proceed to logout
          AuthServiceProvider.logout().then(function() {
            $rootScope.identity = {};
            $rootScope.previousStateName = undefined;
            $rootScope.previousStateParams = undefined;
            $rootScope.$broadcast('user-logged-out');

            // logged out --> navigate to start page
            $state.go('start', {
              lang: LanguageService.getCurrentInstantly()
            }, {
              reload: true
            });
          });
        } else {
          // no token --> login
          AuthServiceProvider.login();
        }
      },
      hasAuthority: function(authority) {
        return (/*uiLoggedIn &&*/ AuthServiceProvider.hasToken() &&
          AuthServiceProvider.accessTokenInfo().scope &&
          AuthServiceProvider.accessTokenInfo().
          scope.indexOf(authority.toLowerCase()) !== -1);
      },
      hasAnyAuthority: function(authorities) {
        if (/*!uiLoggedIn ||*/ !AuthServiceProvider.hasToken()) {
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

        // if (!uiLoggedIn) {
        //   deferred.reject();
        // }
        if (/*uiLoggedIn &&*/ AuthServiceProvider.hasToken()) {
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
        return /*uiLoggedIn &&*/ AuthServiceProvider.hasToken() &&
          AuthServiceProvider.idTokenInfo().preferred_username;
      }
    };
  });
