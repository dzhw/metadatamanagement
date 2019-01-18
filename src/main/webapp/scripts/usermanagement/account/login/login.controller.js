'use strict';

angular.module('metadatamanagementApp').controller('LoginController',
  function($rootScope, $scope, $state, $timeout, $q, Auth, PageTitleService,
           LanguageService, ToolbarHeaderService, WelcomeDialogService,
           Principal) {
    PageTitleService.setPageTitle('user-management.login.title');
    $scope.user = {};
    $scope.errors = {};
    $scope.rememberMe = true;
    $timeout(function() {
      angular.element('[ng-model="username"]').focus();
    });

    var deactivateWelcomeDialog = function() {
      return Principal.identity().then(function(identity) {
        identity.welcomeDialogDeactivated = true;
        return Auth.updateAccount(identity);
      });
    };

    var displayWelcomeDialog = function() {
      $scope.authenticationError = false;

      var deferred = $q.defer();

      if (!Principal.isWelcomeDialogDeactivated() &&
        Principal.hasAuthority('ROLE_DATA_PROVIDER')) {

        WelcomeDialogService.display(Principal.loginName())
          .then(function(hideWelcomeDialog) {
            if (hideWelcomeDialog) {
              return deactivateWelcomeDialog();
            }
          })
          .finally(deferred.resolve);
      } else {
        deferred.resolve();
      }

      return deferred.promise;
    };

    var navigateAfterLogin = function() {
      if ($rootScope.previousStateName === 'register') {
        $state.go('search', {
          lang: LanguageService.getCurrentInstantly()
        });
      } else {
        $rootScope.back();
      }
    };

    $scope.login = function(event) {
      event.preventDefault();
      Auth.login({
        username: $scope.username,
        password: $scope.password,
        rememberMe: $scope.rememberMe
      }).then(displayWelcomeDialog)
        .then(navigateAfterLogin)
        .catch(function() {
          $scope.authenticationError = true;
        });
    };
    ToolbarHeaderService.updateToolbarHeader({
      'stateName': $state.current.name
    });
  }
)
;
