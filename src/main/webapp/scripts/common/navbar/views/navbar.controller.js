/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal, DataAcquisitionProjectCollectionResource,
    CurrentProjectService, DataAcquisitionProjectPostValidationService,
    DataAcquisitionProjectResource) {
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
    $scope.dataAcquisitionProjects = null;
    $scope.project = null;
    $scope.disableAutocomplete = false;

    function checkEmptyListProjects() {
      if ($scope.dataAcquisitionProjects) {

        //It is possible that a project could be deleted after it is selected.
        //This method checks it, because it will be called for disabling the
        //the drop dpwn menu.
        if (!include($scope.dataAcquisitionProjects, $scope.project)) {
          $scope.updateCurrentProject(null);
        }

        //Empty List -> Disable the Drop Down
        $scope.disableAutocomplete = ($scope.dataAcquisitionProjects.length ===
          0);
      }
    }

    //Load the projects for the drop menu
    $scope.loadProjects = function() {
      DataAcquisitionProjectCollectionResource.query({},
        function(result) {
          $scope.dataAcquisitionProjects =
            result._embedded.dataAcquisitionProjects;
          checkEmptyListProjects();
        });
    };
    $scope.loadProjects();

    //Helper method for query the project list
    function createFilterFor(query) {
      var lowercaseQuery = angular.lowercase(query);
      return function filterFn(project) {
        return (project.id.indexOf(lowercaseQuery) === 0);
      };
    }

    //Query for searching in project list
    $scope.querySearch = function(query) {
      var results = query ? $scope.dataAcquisitionProjects.filter(
          createFilterFor(
            query)) :
        $scope.dataAcquisitionProjects;
      return results;
    };

    //Update the state for the current project
    $scope.updateCurrentProject = function(project) {
      $scope.project = project;
      CurrentProjectService.setCurrentProject(project);
    };

    $scope.deleteProject = function() {
      //TODO confirm delete
      DataAcquisitionProjectResource.delete({id: $scope.project.id});
      //TODO toast for success and error autocomplete
      //TODO remove from autocomplete
    };

    $scope.createProject = function() {
      //TODO implement me
    };

    $scope.postValidateProject = function() {
      DataAcquisitionProjectPostValidationService
        .postValidate($scope.project.id);
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
