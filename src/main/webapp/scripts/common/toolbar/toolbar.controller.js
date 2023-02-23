'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $rootScope, $mdSidenav, ShoppingCartService, Principal,
           SearchResultNavigatorService, LanguageService, Auth, $state,
           MessageBus, $location, $timeout, SimpleMessageToastService, DataAcquisitionProjectRepositoryClient) {
    $scope.open = false;
    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.hasAuthority = Principal.hasAuthority;
    $scope.canSwitchViews = Principal.canSwitchViews;
    $scope.isProviderViewActive = Principal.isProviderActive;
    $scope.getInitialView;
    
    $scope.isInitialProvider = function() {
      if (Principal.isDataProvider() && !Principal.isAdmin() && !Principal.isPublisher()){
        return false;
      }
      return true;
    }

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
     * Before switching to provider view check if user has assigned projects.
     * Only users with assigned projects can switch views.
     */
    $scope.switchToProviderView = function() {
      if (Principal.isDataprovider() && !Principal.isPublisher() && !Principal.isAdmin()){
        DataAcquisitionProjectRepositoryClient.findAssignedProjects(
          Principal.loginName()).then(function(response) {
              var projects = response.data;
              if (projects.length === 0) {
                SimpleMessageToastService.openAlertMessageToast('user-management.view-switch.alert.no-assigned-projects', {});
                $scope.switchProviderViewState(false);
                $state.go('searchReleased', {reload: true, notify: true});
              } else {
                $state.go('search', {reload: true, notify: true});
                $scope.switchProviderViewState(true);
              }
            }).catch(
              $state.go('searchReleased', {reload: true, notify: true})
            );
        }
        $state.go('search', {reload: true, notify: true});
        $scope.switchProviderViewState(true);
    }
    $scope.switchToOrderView = function() {
      $state.go('searchReleased', {reload: true, notify: true});
      $scope.switchProviderViewState(false);
    };
    $scope.switchProviderViewState = function(active) {
      if (active) {
        $scope.getInitialView = 'providerView';
        Principal.activateProviderView();
      } else {
        $scope.getInitialView = 'orderView';
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
          $scope.getInitialView = view;
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

    $scope.isDataProvider = function() {
      return Principal.hasAuthority('ROLE_DATA_PROVIDER');
    };

    $scope.showEmptyCart = function() {
      return (!$scope.productsCount && !$scope.isAuthenticated()) ||
        (!$scope.productsCount && $scope.isAuthenticated() &&
        $scope.isDataProvider());
    };

    $scope.showFullCart = function() {
      return ($scope.productsCount  && !$scope.isAuthenticated()) ||
        ($scope.productsCount && $scope.isAuthenticated() &&
        $scope.isDataProvider());
    };
    // $scope.init();
  });
