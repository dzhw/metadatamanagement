'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $rootScope, $mdSidenav, ShoppingCartService, Principal,
           SearchResultNavigatorService, LanguageService, Auth, $state,
           MessageBus) {
    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };

    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.hasAuthority = Principal.hasAuthority;

    //Set Languages
    $scope.changeLanguage = function(languageKey) {
      LanguageService.setCurrent(languageKey);
    };

    //Goto Logout Page
    $scope.logout = function() {
      Auth.logout();
      $state.go('search', {
        lang: LanguageService.getCurrentInstantly(),
        type: 'studies'
      }, {
        reload: true
      });
    };
    $scope.resetQuery = function() {
      $rootScope.searchQuery = '';
      MessageBus.remove('searchFilter');
    };
    $scope.productsCount = ShoppingCartService.count();
    $scope.$on('shopping-cart-changed',
      function(event, count) { // jshint ignore:line
        $scope.productsCount = count;
      });

    $scope.SearchResultNavigatorService = SearchResultNavigatorService;
  });
