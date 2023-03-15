'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $rootScope, $mdSidenav, ShoppingCartService, Principal,
           SearchResultNavigatorService, LanguageService, Auth, $state,
           MessageBus, $location, $timeout) {
    $scope.open = false;
    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.hasAuthority = Principal.hasAuthority;
    $scope.canSwitchViews = Principal.canSwitchViews;
    $scope.isProviderViewActive = Principal.isProviderActive;
    $scope.current = localStorage.getItem('currentView'); // jshint ignore:line

    /**
     * By default users should start in provider view unless
     * they are Dataproviders and nothing more.
     */
    $scope.isInitialProvider = function() {
      if (Principal.isDataprovider() &&
        !Principal.isAdmin() && !Principal.isPublisher()) {
        return false;
      }
      return true;
    };

    //Set Languages
    $scope.changeLanguage = function(languageKey) {
      LanguageService.setCurrent(languageKey);
    };

    //Goto Logout Page
    $scope.logout = function() {
      Auth.logout();
      $scope.resetQuery();
      $state.go('start', {
        lang: LanguageService.getCurrentInstantly()
      }, {
        reload: true
      });
    };
    /**
     * Reset Query and navigate to order view
     */
    $scope.goToOrderPage = function() {
      $scope.resetQuery();
      $scope.switchToOrderView();
    };
    /**
     * Reset Query and navigate to provider view
     */
    $scope.goToProviderPage = function() {
      $scope.resetQuery();
      $scope.switchToProviderView();
    };
    $scope.resetQuery = function() {
      $rootScope.searchQuery = '';
      var searchParams = $location.search();
      if (searchParams && searchParams.hasOwnProperty('query')) {
        delete searchParams.query;
        $location.search(searchParams);
      }
      MessageBus.remove('searchFilter');
    };
    /**
     * Navigate to provider view and switch view state.
     */
    $scope.switchToProviderView = function() {
      $scope.switchProviderViewState(true);
      $state.go('search', {reload: true, notify: true});
    };
    /**
     * Navigate to order view and switch view state.
     */
    $scope.switchToOrderView = function() {
      $scope.switchProviderViewState(false);
      $state.go('searchReleased', {reload: true, notify: true});
    };
    /**
     * Switch the view state. When active is true the provider view is active.
     */
    $scope.switchProviderViewState = function(active) {
      if (active) {
        Principal.activateProviderView();
      } else {
        Principal.deactivateProviderView();
      }
    };
    $scope.productsCount = ShoppingCartService.count();
    $scope.$on('shopping-cart-changed',
      function(event, count) { // jshint ignore:line
        $scope.productsCount = count;
      });

    $scope.$on('view-changed',
      function(event, view) { // jshint ignore:line
        $scope.current = view;
      });

    $scope.SearchResultNavigatorService = SearchResultNavigatorService;
    $timeout(function() {
      $scope.$watch(function() {
        return $mdSidenav('SideNavBar').isOpen();
      }, function() {
        $scope.open = $mdSidenav('SideNavBar').isOpen();
      });
    });

    $scope.$watch(function() {
      return $state.current.name;
    }, function() {
      $scope.show = $state.current.name !== 'start' &&
        $state.current.name !== 'disclosure' &&
        $state.current.name !== 'shoppingCart' &&
        $state.current.name !== 'requestReset' &&
        $state.current.name !== 'activate' &&
        $state.current.name !== 'register' &&
        $state.current.name !== 'login';
    });

    $scope.showEmptyCart = function() {
      return (!$scope.productsCount && !$scope.isAuthenticated()) ||
        (!$scope.productsCount && $scope.isAuthenticated() &&
        Principal.isDataprovider());
    };

    $scope.showFullCart = function() {
      return ($scope.productsCount  && !$scope.isAuthenticated()) ||
        ($scope.productsCount && $scope.isAuthenticated() &&
        Principal.isDataprovider());
    };
  });
