'use strict';

angular.module('metadatamanagementApp').service(
  'ProjectReleaseService',
  function(
    DataAcquisitionProjectPostValidationService,
    SimpleMessageToastService,
    DataAcquisitionProjectResource,
    CurrentProjectService,
    $mdDialog,
    $translate,
    $state
  ) {
    var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
        'data-acquisition-project.';
    var doRelease = function(project) {
      $mdDialog.show({
        controller: 'ReleaseProjectDialogController',
        templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
          'views/release-project-dialog.html.tmpl',
        clickOutsideToClose: false,
        fullscreen: true,
        locals: {
          project: angular.copy(project)
        }
      }).catch(function() {
        // user cancelled
      });
    };

    var releaseProject = function(project) {
      DataAcquisitionProjectPostValidationService
      .postValidate(project.id)
      .then(function() {
        doRelease(project);
      }, function() {
        $mdDialog.show($mdDialog.alert()
        .title($translate.instant(
          i18nPrefix + 'release-not-possible-title', {
            id: project.id
          }))
        .textContent($translate.instant(
          i18nPrefix + 'release-not-possible', {
            id: project.id
          }))
        .ariaLabel($translate.instant(
          i18nPrefix + 'release-not-possible-title', {
            id: project.id
          }))
        .ok($translate.instant('global.buttons.ok')));
      });
    };

    var unreleaseProject = function(project) {
      var confirmDialog = $mdDialog.confirm()
      .title($translate.instant(i18nPrefix + 'unrelease-title', {
        id: project.id
      }))
      .textContent($translate.instant(i18nPrefix + 'unrelease', {
        id: project.id
      }))
      .ariaLabel($translate.instant(i18nPrefix + 'unrelease', {
        id: project.id
      }))
      .ok($translate.instant('global.buttons.ok'))
      .cancel($translate.instant('global.buttons.cancel'));
      $mdDialog.show(confirmDialog).then(function() {
        var projectCopy = angular.copy(project);
        delete projectCopy.release;
        DataAcquisitionProjectResource.save(projectCopy)
        .$promise
        .then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
            i18nPrefix + 'unreleased-successfully', {
              id: project.id
            });
          CurrentProjectService.setCurrentProject(projectCopy);
          $state.forceReload();
        });
      });
    };

    return {
      releaseProject: releaseProject,
      unreleaseProject: unreleaseProject
    };
  }
);
