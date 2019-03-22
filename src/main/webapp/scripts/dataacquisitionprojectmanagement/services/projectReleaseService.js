'use strict';

angular.module('metadatamanagementApp').service(
  'ProjectReleaseService',
  function(
    SimpleMessageToastService,
    DataAcquisitionProjectResource,
    CurrentProjectService,
    $mdDialog,
    $translate,
    $state
  ) {
    var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
        'data-acquisition-project.';
    var releaseProject = function(project) {
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

    var stripVersionSuffix = function(id) {
      if (!id) {
        return id;
      }
      var match = id.match(/-[0-9]+\.[0-9]+\.[0-9]+$/);
      if (match !== null) {
        return id.substr(0, match.index);
      } else {
        return id;
      }
    };

    return {
      releaseProject: releaseProject,
      unreleaseProject: unreleaseProject,
      stripVersionSuffix: stripVersionSuffix
    };
  }
);
