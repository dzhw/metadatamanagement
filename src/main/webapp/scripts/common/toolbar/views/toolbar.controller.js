'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $mdSidenav, $location) {
    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };

    $scope.$location = $location;
  });
