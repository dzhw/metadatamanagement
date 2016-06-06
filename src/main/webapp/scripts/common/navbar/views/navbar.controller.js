'use strict';

angular.module('metadatamanagementApp').controller(
  'NavbarController',
  function($scope, $state, Auth, Principal, ENV, $filter, $mdSidenav) {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.$state = $state;
    $scope.inProductionOrDev = ENV === 'prod' || ENV === 'dev';

    $scope.logout = function() {
      Auth.logout();
      $state.go('home');
    };

    var toggleMenues = [{
      name: 'menu-entities',
      toggle: false
    }];

    $scope.toggleMenues = toggleMenues;

    $scope.isOpen = function() {
      return true;
    };

    $scope.toggle = function($event) {
      console.log($event);
      var toggleElement = $event.currentTarget.attributes[4].value;
      var found = $filter('filter')($scope.toggleMenues, {
        name: toggleElement
      }, true);
      if (found.length) {
        $event.currentTarget.attributes[5].value = !$event.currentTarget
          .attributes[5].value;
        found[0].toggle = !found[0].toggle;
        $mdSidenav('menu-entities').toggle();
      }
    };
  });
