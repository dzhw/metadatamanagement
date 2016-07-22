/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectNavbarController', [
    'CurrentProjectService', 'DataAcquisitionProjectPostValidationService',
    'DataAcquisitionProjectSearchResource', 'DataAcquisitionProjectResource',
    '$mdDialog', 'SimpleMessageToastService', '$translate',
    'CleanJSObjectService',
    function(CurrentProjectService,
      DataAcquisitionProjectPostValidationService,
      DataAcquisitionProjectSearchResource, DataAcquisitionProjectResource,
      $mdDialog, SimpleMessageToastService, $translate, CleanJSObjectService) {
      var ctrl = this;
      //For Project Handling
      ctrl.dataAcquisitionProjects = null;
      ctrl.searchText = '';
      ctrl.disableAutocomplete = false;
      //The used and selected Project. Do not use ctrl.selectedProject!
      ctrl.project = null;

      //This method checks the list, if a project was deleted
      //It disables the autocomplete,
      //if there are no more projects after a deletion
      function checkEmptyListProjects() {
        if (ctrl.dataAcquisitionProjects) {
          //Empty List -> Disable the Drop Down
          ctrl.disableAutocomplete =
            (ctrl.dataAcquisitionProjects.length === 0);
        }
      }

      //helper method
      function include(array, objectInArray) {
        return (array.indexOf(objectInArray) !== -1);
      }

      //It is possible that a project could be deleted after it is selected.
      //This method disable the the drop down menu.
      function deselectProjectAfterDeletion() {
        if (!include(ctrl.dataAcquisitionProjects, ctrl.project)) {
          //Special case for deleted projects
          ctrl.searchText = '';
          ctrl.updateCurrentProject(null);
        }
      }

      //Set the current project again, if e.g. a language change happens and the
      //navbar will be rendered again
      function setCurrentProject() {
        if (!CleanJSObjectService.isNullOrEmpty(
            CurrentProjectService.getCurrentProject())) {
          ctrl.searchText = '';
          ctrl.updateCurrentProject(
            CurrentProjectService.getCurrentProject());
        }
      }

      //Load the projects for the drop menu
      function loadProjects() {
        DataAcquisitionProjectSearchResource.findAll(
          function(result) {
            ctrl.dataAcquisitionProjects =
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
      ctrl.querySearch = function(query) {
        var results = query ? ctrl.dataAcquisitionProjects.filter(
          createFilterFor(query)) : ctrl.dataAcquisitionProjects;
        return results;
      };

      //Update the state for the current project
      ctrl.updateCurrentProject = function(project) {
        ctrl.project = project;
        CurrentProjectService.setCurrentProject(project);
      };

      //Deletes the current project
      ctrl.deleteProject = function() {
        ctrl.showDeleteConfirm();
      };

      /* Function for opening a dialog for creating a new project */
      ctrl.createProject = function() {
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
      ctrl.showDeleteConfirm = function() {
        var confirm = $mdDialog.confirm()
          .title($translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.deleteTitle', {
              name: ctrl.project.id
            }))
          .textContent($translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.delete', {
              name: ctrl.project.id
            }))
          .ariaLabel($translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.delete', {
              name: ctrl.project.id
            }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));

        $mdDialog.show(confirm).then(function() {
          //User clicked okay -> Delete Project, hide dialog, show feedback
          $mdDialog.hide(ctrl.project.id);
          DataAcquisitionProjectResource.delete({
              id: ctrl.project.id
            },
            function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'metadatamanagementApp.' +
                'dataAcquisitionProject.detail.logMessages.' +
                'dataAcquisitionProject.deletedSuccessfullyProject',
                ctrl.project.id);
              deselectProjectAfterDeletion();
              loadProjects();
            },
            function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'metadatamanagementApp.' +
                'dataAcquisitionProject.detail.logMessages.' +
                'dataAcquisitionProject.deletedNotSuccessfullyProject',
                ctrl.project.id);
            });

        }, function() {
          //User clicked cancel
          $mdDialog.cancel();
        });
      };

      ctrl.postValidateProject = function() {
        DataAcquisitionProjectPostValidationService
          .postValidate(ctrl.project.id);
      };
    }
  ]);
