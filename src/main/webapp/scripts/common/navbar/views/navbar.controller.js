'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal, DataAcquisitionProjectCollectionResource) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
    $scope.isEntityMenuOpen = false;
    $scope.isAccountMenuOpen = false;

    $scope.currentProject = null;
    $scope.dataAcquisitionProjects = null;
    $scope.loadProjects = function() {
      DataAcquisitionProjectCollectionResource.query({},
        function(result) {
          $scope.dataAcquisitionProjects =
            result._embedded.dataAcquisitionProjects;
        });
    };

    //Functions for toggling buttons.
    $scope.toggleEntityMenu = function() {
      $scope.isEntityMenuOpen = !$scope.isEntityMenuOpen;
    };

    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

  });
