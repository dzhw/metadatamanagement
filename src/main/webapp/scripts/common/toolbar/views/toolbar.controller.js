'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $state, Auth, Principal, $mdSidenav, Language) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    $scope.changeLanguage = function(languageKey) {
      Language.setCurrent(languageKey);
    };

    //Logout function
    $scope.logout = function() {
      Auth.logout();
      $state.go('home');
    };

    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };
  });
