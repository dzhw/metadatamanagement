/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('dataAcquisitionProjectNavbarController', ['$scope',
    'CurrentProjectService', 'DataAcquisitionProjectPostValidationService',
    'DataAcquisitionProjectSearchResource', 'DataAcquisitionProjectResource',
    '$mdDialog', 'SimpleMessageToastService', '$translate',
    'CleanJSObjectService',
    function($scope, CurrentProjectService,
      DataAcquisitionProjectPostValidationService,
      DataAcquisitionProjectSearchResource, DataAcquisitionProjectResource,
      $mdDialog, SimpleMessageToastService, $translate, CleanJSObjectService) {

      //For Project Handling
      $scope.dataAcquisitionProjects = null;
      $scope.autocomplete = {};
      $scope.autocomplete.searchText = '';
      $scope.disableAutocomplete = false;
      //The used and selected Project. Do not use $scope.selectedProject!
      $scope.project = null;

      //This method checks the list, if a project was deleted
      //It disables the autocomplete,
      //if there are no more projects after a deletion
      function checkEmptyListProjects() {
        if ($scope.dataAcquisitionProjects) {
          //Empty List -> Disable the Drop Down
          $scope.disableAutocomplete =
            ($scope.dataAcquisitionProjects.length === 0);
        }
      }

      //helper method
      function include(array, objectInArray) {
        return (array.indexOf(objectInArray) !== -1);
      }

      //It is possible that a project could be deleted after it is selected.
      //This method disable the the drop down menu.
      function deselectProjectAfterDeletion() {
        if (!include($scope.dataAcquisitionProjects, $scope.project)) {
          //Special case for deleted projects
          $scope.autocomplete.searchText = '';
          $scope.updateCurrentProject(null);
        }
      }

      //Set the current project again, if e.g. a language change happens and the
      //navbar will be rendered again
      function setCurrentProject() {
        if (!CleanJSObjectService.isNullOrEmpty(
            CurrentProjectService.getCurrentProject())) {
          $scope.autocomplete.searchText = '';
          $scope.updateCurrentProject(
            CurrentProjectService.getCurrentProject());
        }
      }

      //Load the projects for the drop menu
      function loadProjects() {
        DataAcquisitionProjectSearchResource.findAll(
          function(result) {
            $scope.dataAcquisitionProjects =
              result._embedded.dataAcquisitionProjects;
            checkEmptyListProjects();
            setCurrentProject();
          });
      }
      loadProjects();

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

      //Deletes the current project
      $scope.deleteProject = function() {
        $scope.showDeleteConfirm();
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
            DataAcquisitionProjectResource.save({
                id: project.name
              },
              //Success
              function() {
                SimpleMessageToastService
                  .openSimpleMessageToast('metadatamanagementApp.' +
                    'dataAcquisitionProject.detail.logMessages.' +
                    'dataAcquisitionProject.saved', project.name);
                loadProjects();
              },
              //Server Error
              function(errorMsg) {
                SimpleMessageToastService
                  .openSimpleMessageToast('metadatamanagementApp.' +
                    'dataAcquisitionProject.detail.logMessages.' +
                    'dataAcquisitionProject.serverError' + errorMsg);
                loadProjects();
              }
            );
          });
      };

      //Conformation Dialog for the deletion of a project
      $scope.showDeleteConfirm = function() {
        var confirm = $mdDialog.confirm()
          .title($translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.deleteTitle', {
              name: $scope.project.id
            }))
          .textContent($translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.delete', {
              name: $scope.project.id
            }))
          .ariaLabel($translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.delete', {
              name: $scope.project.id
            }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));

        $mdDialog.show(confirm).then(function() {
          //User clicked okay -> Delete Project, hide dialog, show feedback
          $mdDialog.hide($scope.project.id);
          DataAcquisitionProjectResource.delete({
              id: $scope.project.id
            },
            function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'metadatamanagementApp.' +
                'dataAcquisitionProject.detail.logMessages.' +
                'dataAcquisitionProject.deletedSuccessfullyProject',
                $scope.project.id);
              deselectProjectAfterDeletion();
              loadProjects();
            },
            function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'metadatamanagementApp.' +
                'dataAcquisitionProject.detail.logMessages.' +
                'dataAcquisitionProject.deletedNotSuccessfullyProject',
                $scope.project.id);
            });

        }, function() {
          //User clicked cancel
          $mdDialog.cancel();
        });
      };

      $scope.postValidateProject = function() {
        DataAcquisitionProjectPostValidationService
          .postValidate($scope.project.id);
      };
    }
  ])
  .directive('projectNavbarModule', function() {
    return {
      restrict: 'E',
      transclude: true,
      scope: {},
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'data-acquisition-project-navbar-module.html.tmpl'
    };
  });
