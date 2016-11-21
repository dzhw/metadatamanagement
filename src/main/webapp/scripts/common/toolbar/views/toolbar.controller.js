'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $state, Auth, Principal, $mdSidenav, LanguageService,
    $mdMedia, PageTitleService) {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.pageTitle = PageTitleService.getPageTitle();

    $scope.$on('page-title-changed', function(event, currentTitle) {
      $scope.pageTitle = currentTitle;
    });

    //Set Languages
    $scope.changeLanguage = function(languageKey) {
      LanguageService.setCurrent(languageKey);
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

    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };
  });
