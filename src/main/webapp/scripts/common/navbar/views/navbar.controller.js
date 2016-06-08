'use strict';

angular.module('metadatamanagementApp').controller(
  'NavbarController',
  function($scope, $state, Auth, Principal, ENV, $mdSidenav, $timeout) {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.$state = $state;
    $scope.inProductionOrDev = ENV === 'prod' || ENV === 'dev';

    $scope.logout = function() {
      Auth.logout();
      $state.go('home');
    };

    /**
     * Supplies a function that will continue to operate until the
     * time is up.
     */
    function debounce(func, wait) {
      var timer;
      return function debounced() {
        var args = Array.prototype.slice.call(arguments);
        console.log('HALLO');
        $timeout.cancel(timer);
        timer = $timeout(function() {
          timer = undefined;
          func.apply($scope, args);
        }, wait || 10);
      };
    }

    /**
     * Build handler to open/close a SideNav; when animation finishes
     * report completion in console
     */
    function buildDelayedToggler(navID) {
      return debounce(function() {
        // Component lookup should always be
        //available since we are not using `ng-if`
        $mdSidenav(navID)
          .toggle()
          .then(function() {
            console.log('toggle ' + navID + ' is done');
          });
      }, 200);
    }

    $scope.toggleLeft = buildDelayedToggler('left');
  });
