/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('SidenavController',
  function($scope, $rootScope, Principal, $mdSidenav, $document, $timeout,
           LanguageService, Auth, $state, MessageBus,
           WelcomeDialogService) {

    $scope.isAuthenticated = Principal.isAuthenticated;
    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
    $scope.isAccountMenuOpen = false;
    $scope.isOrderMenuOpen = false;
    $scope.logoutButtonDisabled = false;
    $scope.sidebarContent = $rootScope.sidebarContent;
    $scope.detailViewLoaded = MessageBus;
    $scope.onCloseOrderMenu = MessageBus;
    $scope.detailViewType = '';
    $scope.show = false;

    $scope.$on('domain-object-editing-started', function() {
      $scope.logoutButtonDisabled = true;
    });

    $scope.$on('domain-object-editing-stopped', function() {
      $scope.logoutButtonDisabled = false;
    });

    // $rootScope.$on('onDataPackageLoaded',
    //   function(event, args) { // jshint ignore:line
    //   $scope.dataPackage = args.dataPackage;
    //   $scope.accessWays = args.accessWays;
    // });

    //Functions for toggling buttons.
    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

    $scope.toggleOrderMenu = function() {
      $scope.isOrderMenuOpen = !$scope.isOrderMenuOpen;
    };

    $scope.close = function(timeout) {
      $timeout(function() {
        if (!$mdSidenav('SideNavBar').isLockedOpen()) {
          $mdSidenav('SideNavBar').toggle();
        }
      }, timeout ? timeout : 1000);
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
        $state.current.name !== 'restoreShoppingCart' &&
        $state.current.name !== 'requestReset' &&
        $state.current.name !== 'finishReset' &&
        $state.current.name !== 'activate' &&
        $state.current.name !== 'register' &&
        $state.current.name !== 'login';
      if (!$scope.show && $mdSidenav('SideNavBar').isOpen()) {
        $scope.close();
      }
    });

    $scope.$watch(function() {
        return $scope.detailViewLoaded;
      },
      function() {
        var data = $scope.detailViewLoaded.get('onDetailViewLoaded', true);
        if (data) {
          // close order menu if the detail view type changed
          if ($scope.detailViewType !== data.type) {
            $scope.isOrderMenuOpen = false;
          }
          $scope.detailViewType = data.type;
        }
      }, true);

    $scope.$watch(function() {
      // listen for close triggers
      return $scope.onCloseOrderMenu;
    },
    function() {
      var data = $scope.onCloseOrderMenu.get('onCloseOrderMenu', true);
      if (data) {
        $scope.isOrderMenuOpen = data.open;
      }
    }, true);
  });
