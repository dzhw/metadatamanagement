'use strict';

angular.module('metadatamanagementApp').service('ProjectReleaseService', [
  'SimpleMessageToastService', 
  'DataAcquisitionProjectResource', 
  'CurrentProjectService', 
  'MonitoringService',
  '$mdDialog', 
  '$translate', 
  '$state', 
  function(
    SimpleMessageToastService,
    DataAcquisitionProjectResource,
    CurrentProjectService,
    MonitoringService,
    $mdDialog,
    $translate,
    $state
  ) {

    var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
        'data-acquisition-project.';
    const i18nPrefixDialog = 'data-acquisition-project-management.release.pid-api-not-reachable-dialog.';

    var releaseProject = async function(project) {

      const pidApiAvailable = await MonitoringService.checkDaraPidHealth();
      const pidApiUnavailableConfirmDialog = $mdDialog.confirm()
        .title($translate.instant(i18nPrefixDialog + 'title'))
        .textContent(
          $translate.instant(i18nPrefixDialog + 'message') + '\n\n' + $translate.instant(i18nPrefixDialog + 'question')
        )
        .ariaLabel(
          $translate.instant(i18nPrefixDialog + 'message') + $translate.instant(i18nPrefixDialog + 'question')
        )
        .ok($translate.instant('global.common-dialogs.yes'))
        .cancel($translate.instant('global.common-dialogs.no'));

      var continueRelease = Promise.resolve();
      if (!pidApiAvailable) {
        continueRelease = $mdDialog.show(pidApiUnavailableConfirmDialog);
      }

      const releaseDialogConfig = {
        controller: 'ReleaseProjectDialogController',
        templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
          'views/release-project-dialog.html.tmpl',
        clickOutsideToClose: false,
        fullscreen: true,
        locals: {
          project: angular.copy(project)
        }
      };
      continueRelease
        .then(() => $mdDialog.show(releaseDialogConfig).catch(() => {}))
        .catch(() => {});
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
  }]);

