/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectNavbarController', [
    'CurrentProjectService', 'DataAcquisitionProjectPostValidationService',
    'DataAcquisitionProjectSearchResource', 'DataAcquisitionProjectResource',
    '$mdDialog', 'SimpleMessageToastService', '$translate',
    'ElasticSearchAdminService', '$location', '$filter',
    function(CurrentProjectService,
      DataAcquisitionProjectPostValidationService,
      DataAcquisitionProjectSearchResource, DataAcquisitionProjectResource,
      $mdDialog, SimpleMessageToastService, $translate,
      ElasticSearchAdminService, $location, $filter) {
      var ctrl = this;
      //For Project Handling
      ctrl.dataAcquisitionProjects = null;
      ctrl.searchText = '';
      function selectProject(project) {
        if (project) {
          ctrl.selectedProject = project;
        } else {
          ctrl.selectedProject = null;
          ctrl.searchText = '';
        }
      }

      //Load the projects for the drop menu
      function loadProjects() {
        var rdcId = $location.search()['rdc-project'];
        DataAcquisitionProjectSearchResource.findAll(
          function(result) {
            ctrl.dataAcquisitionProjects =
              result._embedded.dataAcquisitionProjects;
            ctrl.selectedProject = $filter('filter')(
              ctrl.dataAcquisitionProjects, function(project) {
                return project.id === rdcId;
              })[0];
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
      ctrl.searchProjects = function(query) {
        $location.search('rdc-project', query);
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
                  .openSimpleMessageToast('metadatamanagementApp.' +
                    'dataAcquisitionProject.detail.logMessages.' +
                    'dataAcquisitionProject.saved', {id: project.id});
                loadProjects();
                selectProject(project);
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
      ctrl.deleteProject = function() {
        var confirm = $mdDialog.confirm()
          .title($translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.deleteTitle', {
              id: ctrl.selectedProject.id
            }))
          .textContent($translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.delete', {
              id: ctrl.selectedProject.id
            }))
          .ariaLabel($translate.instant('metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.delete', {
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
              ElasticSearchAdminService.processUpdateQueue().then(function() {
                SimpleMessageToastService.openSimpleMessageToast(
                  'metadatamanagementApp.' +
                  'dataAcquisitionProject.detail.logMessages.' +
                  'dataAcquisitionProject.deletedSuccessfullyProject',
                  {id: ctrl.selectedProject.id});
                loadProjects();
                selectProject(null);
              });
            },
            function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'metadatamanagementApp.' +
                'dataAcquisitionProject.detail.logMessages.' +
                'dataAcquisitionProject.deletedNotSuccessfullyProject',
                {id: ctrl.selectedProject.id});
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
    }
  ]);
