'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $state, Auth, Principal, $mdSidenav, LanguageService) {
    $scope.isAuthenticated = Principal.isAuthenticated;

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
    };

    //Goto Login Page
    $scope.login = function() {
      $state.go('login', {
        lang: LanguageService.getCurrentInstantly()
      });
    };

    //Register function
    $scope.register = function() {
      $state.go('register', {
        lang: LanguageService.getCurrentInstantly()
      });
    };

    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };
  });
