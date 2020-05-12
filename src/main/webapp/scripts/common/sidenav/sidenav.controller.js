/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('SidenavController',
  function($scope, $rootScope, Principal, $mdSidenav, $document, $timeout,
           LanguageService, Auth, $state,
           WelcomeDialogService) {

    $scope.isAuthenticated = Principal.isAuthenticated;
    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
    $scope.isAccountMenuOpen = false;
    $scope.logoutButtonDisabled = false;
    $scope.sidebarContent = $rootScope.sidebarContent;
    $scope.show = false;

    $scope.$on('domain-object-editing-started', function() {
      $scope.logoutButtonDisabled = true;
    });

    $scope.$on('domain-object-editing-stopped', function() {
      $scope.logoutButtonDisabled = false;
    });

    $rootScope.$on('onStudyLoaded',
      function(event, args) { // jshint ignore:line
      $scope.study = args.study;
      $scope.accessWays = args.accessWays;
    });

    //Functions for toggling buttons.
    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

    $scope.close = function() {
      $timeout(function() {
        if (!$mdSidenav('SideNavBar').isLockedOpen()) {
          $mdSidenav('SideNavBar').toggle();
        }
      }, 1000);
    };

    $scope.focusContent = function() {
      $document.find('.fdz-content')[0].focus();
    };

    //Goto Logout Page
    $scope.logout = function() {
      Auth.logout();
      $rootScope.searchQuery = '';
      $state.go('start', {
        lang: LanguageService.getCurrentInstantly()
      }, {
        reload: true
      });
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

    $scope.$watch(function() {
      return $rootScope.sidebarContent;
    }, function() {
      $scope.sidebarContent = $rootScope.sidebarContent;
    }, true);

    $scope.$watch(function() {
      return $state.current.name;
    }, function() {
      $scope.show = $state.current.name !== '' &&
        $state.current.name !== 'start' &&
        $state.current.name !== 'disclosure' &&
        $state.current.name !== 'shoppingCart' &&
        $state.current.name !== 'requestReset' &&
        $state.current.name !== 'activate' &&
        $state.current.name !== 'register' &&
        $state.current.name !== 'login';
      if (!$scope.show && $mdSidenav('SideNavBar').isOpen()) {
        $scope.close();
      }
    });
  });
