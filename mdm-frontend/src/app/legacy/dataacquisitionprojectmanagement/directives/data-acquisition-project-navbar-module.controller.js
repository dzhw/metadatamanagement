/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectNavbarController', [
    'CurrentProjectService', 'DataAcquisitionProjectPostValidationService',
    'DataAcquisitionProjectRepositoryClient', 'DataAcquisitionProjectResource',
    '$mdDialog', 'SimpleMessageToastService', '$translate',
    'ElasticSearchAdminService', '$scope', 'Principal', 'ProjectReleaseService',
    '$state', 'LanguageService',
    function(CurrentProjectService,
             DataAcquisitionProjectPostValidationService,
             DataAcquisitionProjectRepositoryClient,
             DataAcquisitionProjectResource, $mdDialog,
             SimpleMessageToastService, $translate, ElasticSearchAdminService,
             $scope, Principal, ProjectReleaseService, $state,
             LanguageService) {
      var ctrl = this;
      ctrl.hasAuthority = Principal.hasAuthority;
      var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
        'data-acquisition-project.';
      ctrl.searchText = '';
      ctrl.selectedProject = CurrentProjectService.getCurrentProject();

      function showErrorAlert(errorMsg) {
        SimpleMessageToastService
          .openAlertMessageToast(
            i18nPrefix + 'server-error' + errorMsg);
      }

      $scope.$on('current-project-changed',
        function(event, project) { // jshint ignore:line
          ctrl.selectedProject = project;
        });

      $scope.$on('domain-object-editing-started',
        function() {
          ctrl.projectChooserDisabled = true;
        });

      $scope.$on('domain-object-editing-stopped',
        function() {
          ctrl.projectChooserDisabled = false;
        });

      //Query for searching in project list
      ctrl.searchProjects = function(query) {
        return DataAcquisitionProjectRepositoryClient
          .findByIdLikeOrderByIdAsc(query).then(function(result) {
            return result.data.dataAcquisitionProjects;
          });
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
          clickOutsideToClose: false,
          fullscreen: true,
          locals: {id: ctrl.selectedProject ? null : ctrl.searchText}
        })
          .then(function(project) {
            if (angular.isUndefined(project.masterId)) {
              project.masterId = project.id;
            }
            Principal.identity().then(function(identity) {
              project.hasBeenReleasedBefore = false;
              project.configuration = {
                publishers: [identity.login],
                requirements: {
                  dataPackagesRequired: true
                }
              };
              project.assigneeGroup = 'PUBLISHER';
              DataAcquisitionProjectResource.save(project,
                //Success
                function() {
                  SimpleMessageToastService
                    .openSimpleMessageToast(
                      i18nPrefix + 'saved', {
                        id: project.id
                      });
                  ctrl.selectedProject = project;
                  CurrentProjectService.setCurrentProject(project);
                  $state.go('project-cockpit', {
                    id: ctrl.selectedProject.id,
                    lang: LanguageService.getCurrentInstantly(),
                    tab: 'config'},
                    {inherit: false}
                  );
                },
                //Server Error while creating project
                showErrorAlert
              );
              //Server error while querying for Principal
            }, showErrorAlert);
          });
      };

      //Conformation Dialog for the deletion of a project
      ctrl.deleteProject = function() {
        var confirm = $mdDialog.confirm()
          .title($translate.instant(
            i18nPrefix + 'delete-title', {
              id: ctrl.selectedProject.id
            }))
          .textContent($translate.instant(
            i18nPrefix + 'delete', {
              id: ctrl.selectedProject.id
            }))
          .ariaLabel($translate.instant(
            i18nPrefix + 'delete', {
              id: ctrl.selectedProject.id
            }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));

        $mdDialog.show(confirm).then(function() {
          //User clicked okay -> Delete Project, hide dialog, show feedback
          $mdDialog.hide();
          DataAcquisitionProjectResource.delete({
              id: ctrl.selectedProject.id
            },
            function() {
              ElasticSearchAdminService.processUpdateQueue().then(
                function() {
                  SimpleMessageToastService.openSimpleMessageToast(
                    i18nPrefix + 'deleted-successfully-project', {
                      id: ctrl.selectedProject.id
                    });
                  ctrl.selectedProject = null;
                });
            },
            function() {
              SimpleMessageToastService.openAlertMessageToast(
                i18nPrefix + 'deleted-not-successfully-project', {
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

      ctrl.toggleReleaseProject = function() {
        if (ctrl.selectedProject.release) {
          ProjectReleaseService.unreleaseProject(ctrl.selectedProject);
        } else {
          ProjectReleaseService.releaseProject(ctrl.selectedProject);
        }
      };

      // init the input controle with the current project
      if (CurrentProjectService.getCurrentProject()) {
        ctrl.searchProjects(CurrentProjectService.getCurrentProject().id)
          .then(function(projects) {
            if (projects.length === 1) {
              ctrl.selectedProject = projects[0];
            }
          });
      }
    }
  ]);
