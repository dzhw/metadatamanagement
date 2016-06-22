/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal, DataAcquisitionProjectCollectionResource,
    CurrentProjectService) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
    $scope.isEntityMenuOpen = false;
    $scope.isAccountMenuOpen = false;

    //helper method
    function include(array, objectInArray) {
      return (array.indexOf(objectInArray) !== -1);
    }

    //For Project Handling
    $scope.project = null;
    $scope.dataAcquisitionProjects = null;
    $scope.checkEmptyListProjects = function() {
      if ($scope.dataAcquisitionProjects) {

        //It is possible that a project could be deleted after it is selected.
        //This method checks it, because it will be called for disabling the
        //the drop dpwn menu.
        if (!include($scope.dataAcquisitionProjects, $scope.project)) {
          $scope.updateCurrentProject(null);
        }

        //Empty List -> Disable the Drop Down
        return $scope.dataAcquisitionProjects.length === 0;
      }
    };

    //Load the projects for the drop menu
    $scope.loadProjects = function() {
      DataAcquisitionProjectCollectionResource.query({},
        function(result) {
          $scope.dataAcquisitionProjects =
            result._embedded.dataAcquisitionProjects;
          $scope.checkEmptyListProjects();
        });
    };
    $scope.loadProjects();

    //Update the state for the current project
    $scope.updateCurrentProject = function(project) {
      $scope.project = project;
      CurrentProjectService.setCurrentProject(project);
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
