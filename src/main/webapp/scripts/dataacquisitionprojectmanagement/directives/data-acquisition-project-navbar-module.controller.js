/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataAcquisitionProjectNavbarController', [
    'CurrentProjectService', 'DataAcquisitionProjectPostValidationService',
    'DataAcquisitionProjectRepositoryClient', 'DataAcquisitionProjectResource',
    '$mdDialog', 'SimpleMessageToastService', '$translate',
    'ElasticSearchAdminService', '$scope', 'Principal', '$state',
    function(CurrentProjectService,
      DataAcquisitionProjectPostValidationService,
      DataAcquisitionProjectRepositoryClient, DataAcquisitionProjectResource,
      $mdDialog, SimpleMessageToastService, $translate,
      ElasticSearchAdminService, $scope, Principal, $state) {
      var ctrl = this;
      ctrl.hasAuthority = Principal.hasAuthority;
      var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
        'data-acquisition-project.';
      ctrl.searchText = '';
      ctrl.selectedProject = CurrentProjectService.getCurrentProject();

      function alertError(errorMsg) {
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
            return result.data;
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
            Principal.identity().then(function (identity) {
              project.hasBeenReleasedBefore = false;
              project.configuration = {
                publishers: [identity.login]
              };
              DataAcquisitionProjectResource.save(project,
                //Success
                function () {
                  SimpleMessageToastService
                    .openSimpleMessageToast(
                      i18nPrefix + 'saved', {
                        id: project.id
                      });
                  ctrl.selectedProject = project;
                  CurrentProjectService.setCurrentProject(project);
                },
                //Server Error while creating project
                alertError
              );
              //Server error while querying for Principal
            }, alertError);
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

      var doRelease = function() {
        $mdDialog.show({
          controller: 'ReleaseProjectDialogController',
          templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
            'views/release-project-dialog.html.tmpl',
          clickOutsideToClose: false,
          fullscreen: true,
          locals: {
            project: angular.copy(ctrl.selectedProject)
          }
        }).catch(function() {
          // user cancellled
        });
      };

      var releaseProject = function() {
        DataAcquisitionProjectPostValidationService
          .postValidate(ctrl.selectedProject.id)
          .then(doRelease, function() {
            $mdDialog.show($mdDialog.alert()
              .title($translate.instant(
                i18nPrefix + 'release-not-possible-title', {
                  id: ctrl.selectedProject.id
                }))
              .textContent($translate.instant(
                i18nPrefix + 'release-not-possible', {
                  id: ctrl.selectedProject.id
                }))
              .ariaLabel($translate.instant(
                i18nPrefix + 'release-not-possible-title', {
                  id: ctrl.selectedProject.id
                }))
              .ok($translate.instant('global.buttons.ok')));
          });
      };

      var unreleaseProject = function() {
        var confirmDialog = $mdDialog.confirm()
          .title($translate.instant(i18nPrefix + 'unrelease-title', {
            id: ctrl.selectedProject.id
          }))
          .textContent($translate.instant(i18nPrefix + 'unrelease', {
            id: ctrl.selectedProject.id
          }))
          .ariaLabel($translate.instant(i18nPrefix + 'unrelease', {
            id: ctrl.selectedProject.id
          }))
          .ok($translate.instant('global.buttons.ok'))
          .cancel($translate.instant('global.buttons.cancel'));
        $mdDialog.show(confirmDialog).then(function() {
          var projectCopy = angular.copy(ctrl.selectedProject);
          delete projectCopy.release;
          DataAcquisitionProjectResource.save(projectCopy)
            .$promise
            .then(function() {
              SimpleMessageToastService.openSimpleMessageToast(
                i18nPrefix + 'unreleased-successfully', {
                  id: ctrl.selectedProject.id
                });
              CurrentProjectService.setCurrentProject(projectCopy);
              $state.forceReload();
            });
        });
      };

      ctrl.toggleReleaseProject = function() {
        if (ctrl.selectedProject.release) {
          unreleaseProject();
        } else {
          releaseProject();
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
