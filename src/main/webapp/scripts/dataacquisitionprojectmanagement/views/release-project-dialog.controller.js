'use strict';

angular.module('metadatamanagementApp')
  .controller('ReleaseProjectDialogController', function($scope, $mdDialog,
    project, SimpleMessageToastService, DataAcquisitionProjectResource,
    DaraReleaseResource, $rootScope, CurrentProjectService,
    DataAcquisitionProjectLastReleaseResource) {
    $scope.bowser = $rootScope.bowser;
    $scope.project = project;
    var ctrl = this;
    var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
      'data-acquisition-project.';
    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    ctrl.saveProject = function(project) {
      DataAcquisitionProjectResource.save(project).$promise
        .then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
            i18nPrefix + 'released-beta-successfully', {
              id: project.id
            });
          CurrentProjectService.setCurrentProject(project);
        });
    };

    $scope.ok = function(release) {
      DataAcquisitionProjectLastReleaseResource.get({id: project.id})
      .$promise.then(function(lastRelease) {
        var compareForBeta = $scope.bowser
          .compareVersions(['1.0.0', release.version]);
        var compareForHigherVersion = -1;
        if (lastRelease.version) {
          compareForHigherVersion = $scope.bowser
          .compareVersions([lastRelease.version, release.version]);
        }

        //No higher version -> always break up
        //1 = old Version is higher, 0 = Same Version Number
        if (compareForHigherVersion !== -1) {
          console.log('No Higher Version!');
          SimpleMessageToastService.openSimpleMessageToast(
            i18nPrefix + 'no-higher-version', {
              lastReleasedVersion: lastRelease.version
            });
        } else {
          if (compareForBeta === 1) {
            //BETA RELEASE
            release.date = new Date().toISOString();
            project.release = release;
            project.hasBeenReleasedBefore = true;
            ctrl.saveProject(project);
            $mdDialog.hide();
          } else {
            //REGULAR RELEASE
            release.date = new Date().toISOString();
            project.release = release;
            DaraReleaseResource.release(project)
              .$promise.then(function() {
              project.hasBeenReleasedBefore = true;
              ctrl.saveProject(project);
            }).catch(function() {
              delete project.release;
              SimpleMessageToastService.openSimpleMessageToast(
                i18nPrefix + 'dara-released-not-successfully', {
                  id: project.id
                });
            });
            $mdDialog.hide();
          }
        }
      });
    };
  });
