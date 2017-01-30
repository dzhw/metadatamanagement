/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectNavbarController', [
    'CurrentProjectService', 'DataAcquisitionProjectPostValidationService',
    'DataAcquisitionProjectSearchResource', 'DataAcquisitionProjectResource',
    '$mdDialog', 'SimpleMessageToastService', '$translate',
    'ElasticSearchAdminService',
    function(CurrentProjectService,
      DataAcquisitionProjectPostValidationService,
      DataAcquisitionProjectSearchResource, DataAcquisitionProjectResource,
      $mdDialog, SimpleMessageToastService, $translate,
      ElasticSearchAdminService) {
      var ctrl = this;
      //For Project Handling
      ctrl.dataAcquisitionProjects = null;
      ctrl.searchText = '';

      // init the input controle with the current project
      if (CurrentProjectService.getCurrentProject()) {
        ctrl.searchText = CurrentProjectService.getCurrentProject().id;
      }
      //Load the projects for the drop menu
      function loadProjects() {
        DataAcquisitionProjectSearchResource.findAll(
          function(result) {
            ctrl.dataAcquisitionProjects =
              result._embedded.dataAcquisitionProjects;
            var projects = ctrl.searchProjects(ctrl.searchText);
            if (projects.length === 1) {
              ctrl.selectedProject = projects[0];
              ctrl.searchText = projects[0].id;
            } else {
              ctrl.selectedProject = null;
              ctrl.searchText = '';
            }
          });
      }
      //Helper method for query the project list
      function createFilterFor(query) {
        var lowercaseQuery = angular.lowercase(query);
        return function filterFn(project) {
          return (project.id.indexOf(lowercaseQuery) === 0);
        };
      }

      //Query for searching in project list
      ctrl.searchProjects = function(query) {
        var results = query ? ctrl.dataAcquisitionProjects.filter(
          createFilterFor(query)) : ctrl.dataAcquisitionProjects;
        return results;
      };

      //Update the state for the current project
      ctrl.onSelectedProjectChanged = function(project) {
        CurrentProjectService.setCurrentProject(project);
      };

      /* Function for opening a dialog for creating a new project */
      ctrl.createProject = function() {
        $mdDialog.show({
            controller: 'CreateProjectDialogController',
            templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
              'views/create-project-dialog.html.tmpl',
            clickOutsideToClose: false
          })
          .then(function(project) {
            DataAcquisitionProjectResource.save(project,
              //Success
              function() {
                SimpleMessageToastService
                  .openSimpleMessageToast(
                    'data-acquisition-project-management.log-messages.' +
                    'data-acquisition-project.saved', {
                      id: project.id
                    });
                ctrl.selectedProject = project;
                CurrentProjectService.setCurrentProject(project);
                loadProjects();
              },
              //Server Error
              function(errorMsg) {
                SimpleMessageToastService
                  .openSimpleMessageToast(
                    'data-acquisition-project-management.log-messages.' +
                    'data-acquisition-project.server-error' + errorMsg);
                loadProjects();
              }
            );
          });
      };

      //Conformation Dialog for the deletion of a project
      ctrl.deleteProject = function() {
        var confirm = $mdDialog.confirm()
          .title($translate.instant(
            'data-acquisition-project-management.log-messages.' +
            'data-acquisition-project.delete-title', {
              id: ctrl.selectedProject.id
            }))
          .textContent($translate.instant(
            'data-acquisition-project-management.log-messages.' +
            'data-acquisition-project.delete', {
              id: ctrl.selectedProject.id
            }))
          .ariaLabel($translate.instant(
            'data-acquisition-project-management.log-messages.' +
            'data-acquisition-project.delete', {
              id: ctrl.selectedProject.id
            }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));

        $mdDialog.show(confirm).then(function() {
          //User clicked okay -> Delete Project, hide dialog, show feedback
          $mdDialog.hide(ctrl.selectedProject.id);
          DataAcquisitionProjectResource.delete({
              id: ctrl.selectedProject.id
            },
            function() {
              ElasticSearchAdminService.processUpdateQueue().then(
                function() {
                  SimpleMessageToastService.openSimpleMessageToast(
                    'data-acquisition-project-management.log-messages.' +
                    'data-acquisition-project.deleted-successfully-project', {
                      id: ctrl.selectedProject.id
                    });
                  loadProjects();
                });
            },
            function() {
              SimpleMessageToastService.openSimpleMessageToast(

                'data-acquisition-project-management.log-messages.' +
                'data-acquisition-project.deleted-not-successfully-project', {
                  id: ctrl.selectedProject.id
                });
            });

        }, function() {
          //User clicked cancel
          $mdDialog.cancel();
        });
      };

      ctrl.postValidateProject = function() {
        DataAcquisitionProjectPostValidationService
          .postValidate(ctrl.selectedProject.id);
      };
      loadProjects();
    }
  ]);
