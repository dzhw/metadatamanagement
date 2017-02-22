/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal, $mdSidenav, $document, $timeout) {
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
        $timeout($mdSidenav('SideNavBar').toggle, 200);
      }
    };

    var findFirstFocusableElement = function(element) {
      var focusableChild;
      if (element.tabIndex != null && element.tabIndex > -1 &&
        // IE gives ALL elements a tabindex === 0 therefore we limit
        // to the following list of tags
        (element.nodeName === 'A' || element.nodeName === 'BUTTON' ||
        element.nodeName === 'INPUT' || element.nodeName === 'SELECT' ||
        element.nodeName === 'AREA' || element.nodeName === 'TEXTAREA')) {
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
