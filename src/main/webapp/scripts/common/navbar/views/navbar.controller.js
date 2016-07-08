/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal,
    CurrentProjectService, DataAcquisitionProjectPostValidationService,
    DataAcquisitionProjectSearchResource, DataAcquisitionProjectResource,
    $mdDialog, JobCompleteToastService, JobLoggingService, $translate) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
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
      DataAcquisitionProjectSearchResource.query({},
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
          createFilterFor(query)) : $scope.dataAcquisitionProjects;
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

    /* Function for opening a dialog for creating a new project */
    $scope.createProject = function() {
      $mdDialog.show({
        controller: 'CreateProjectDialogController',
        templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
          'views/create-project-dialog.html.tmpl',
        clickOutsideToClose: true
      })
      .then(function(project) {
        JobLoggingService.start();
        DataAcquisitionProjectResource.save({id: project.name},
          //Success
          function() {
            JobLoggingService
              .success($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.dataAcquisitionProject.saved', {
              name: project.name
            }));
            $scope.loadProjects();
            JobLoggingService
              .finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.dataAcquisitionProject.finished', {
              name: project.name
            }));
            JobCompleteToastService.openJobCompleteToast();
          },
          //Server Error
          function(errorMsg) {
            JobLoggingService.start();
            JobLoggingService
              .error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.dataAcquisitionProject.serverError') + errorMsg);
            $scope.loadProjects();
            JobLoggingService
              .finish($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.dataAcquisitionProject.finished', {
              name: project.name
            }));
            JobCompleteToastService.openJobCompleteToast();
          }
        );
      });
    };

    $scope.postValidateProject = function() {
      DataAcquisitionProjectPostValidationService
        .postValidate($scope.project.id);
    };

    //Functions for toggling buttons.
    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

  });
