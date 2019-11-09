/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, $rootScope, Principal, $mdSidenav, $document, $timeout,
           LanguageService, Auth, $state,
           WelcomeDialogService) {

    $scope.isAuthenticated = Principal.isAuthenticated;

    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
    $scope.isAccountMenuOpen = false;

    $scope.changeLanguageButtonDisabled = false;
    $scope.logoutButtonDisabled = false;

    $scope.$on('domain-object-editing-started', function() {
      $scope.changeLanguageButtonDisabled = true;
      $scope.logoutButtonDisabled = true;
    });

    $scope.$on('domain-object-editing-stopped', function() {
      $scope.changeLanguageButtonDisabled = false;
      $scope.logoutButtonDisabled = false;
    });
    this.$onInit = function() {
      if ($scope.$parent.toState.hasOwnProperty('name')) {
        $scope.stateName = 'search';
      }
      $scope.$watch('$parent.toState.name', function() {
        $scope.stateName = $scope.$parent.toState.name;
      });
    };

    //Functions for toggling buttons.
    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

    $scope.close = function() {
      if (!$mdSidenav('SideNavBar').isLockedOpen()) {
        $timeout($mdSidenav('SideNavBar').toggle, 200);
      }
    };

    $scope.focusContent = function() {
      $document.find('.fdz-content')[0].focus();
    };

    //Set Languages
    $scope.changeLanguage = function(languageKey) {
      LanguageService.setCurrent(languageKey);
    };

    //Goto Logout Page
    $scope.logout = function() {
      Auth.logout();
      $state.go('search', {
        lang: LanguageService.getCurrentInstantly()
      });
      $scope.close();
    };

    $scope.displayWelcomeDialog = function() {
      WelcomeDialogService.display(Principal.loginName(), false);
    };

    $scope.isDataProvider = function() {
      return Principal.hasAuthority('ROLE_DATA_PROVIDER');
    };

    var fixTextareaHeight = function() {
      $timeout(function() {
        $scope.$broadcast('md-resize-textarea');
      }, 100);
    };

    $scope.$watch(function() {
      return $mdSidenav('SideNavBar').isLockedOpen();
    }, function() {
      fixTextareaHeight();
    }, true);
  });
