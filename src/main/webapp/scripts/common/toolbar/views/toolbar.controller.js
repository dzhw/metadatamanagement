'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $state, Auth, Principal, $mdSidenav, Language, $mdMedia, ShoppingCartService) {
    $scope.isAuthenticated = Principal.isAuthenticated;
    ShoppingCartService.getShoppingCart().then(function(basket) {
      $scope.itemsInBasket = basket.length;
    });
    $scope.$on('itemsCount', function(event, args) {
      $scope.itemsInBasket = args;
    });
    //Set Languages
    $scope.changeLanguage = function(languageKey) {
      Language.setCurrent(languageKey);
    };

    $scope.$mdMedia = $mdMedia;

    //Goto Logout Page
    $scope.logout = function() {
      Auth.logout();
      $state.go('search');
    };

    //Goto Login Page
    $scope.login = function() {
      $state.go('login');
    };

    //Register function
    $scope.register = function() {
      $state.go('register');
    };

    //Basket function
    $scope.basket = function() {
      $state.go('basket');
    };

    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };
  });
