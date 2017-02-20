/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal, $mdSidenav, $document) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
    $scope.isAccountMenuOpen = false;

    //Functions for toggling buttons.
    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

    $scope.close = function() {
      if (!$mdSidenav('SideNavBar').isLockedOpen()) {
        $mdSidenav('SideNavBar').toggle();
      }
    };

    var findFirstFocusableElement = function(element) {
      var focusableChild;
      if (element.tabIndex != null && element.tabIndex > -1) {
        return element;
      } else {
        for (var i = 0; i < element.children.length; i++) {
          focusableChild = findFirstFocusableElement(element.children[i]);
          if (focusableChild) {
            return focusableChild;
          }
        }
      }
    };

    $scope.focusContent = function() {
      var content = $document.find('#content')[0];
      var element = findFirstFocusableElement(content);
      if (element) {
        element.focus();
      }
    };
  });
