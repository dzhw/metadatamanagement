'use strict';

angular.module('metadatamanagementApp')
  .controller('ReleaseProjectDialogController', function($scope, $mdDialog,
    project, SimpleMessageToastService, DataAcquisitionProjectResource,
    DaraReleaseResource, $rootScope, CurrentProjectService) {
    $scope.bowser = $rootScope.bowser;
    $scope.project = project;
    var ctrl = this;
    var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
      'data-acquisition-project.';
    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    ctrl.getLastReleasedVersion = function() {
      /* TODO DKatzberg: Faked Return...
      DataAcquisitionProjectLastReleasedVersionResource.lastReleasedVersion({
          id: ctrl.selectedProject.id
        })
      .then(function(lastReleasedVersion) {
        if (lastReleasedVersion.length === 0) {
          return '0.0.0';
        } else {
          return JSON.stringify(lastReleasedVersion);
        }
      });*/
      return '0.5.0';
    };

    $scope.ok = function(release) {
      var lastReleasedVersion =  ctrl.getLastReleasedVersion();
      var compareForBeta = $scope.bowser
        .compareVersions(['1.0.0', release.version]);
      var compareForHigherVersion = $scope.bowser
        .compareVersions([lastReleasedVersion, release.version]);

      //No higher version -> always break up
      //1 = old Version is higher, 0 = Same Version Number
      if (compareForHigherVersion === 1 || compareForHigherVersion === 0) {
        //TODO DKatzberg NO HIGHER VERSION -> NO RELEASE
        console.log('No Higher Version!');
      } else {
        if (compareForBeta === 1) {
          //BETA RELEASE
          release.date = new Date().toISOString();
          project.release = release;
          DataAcquisitionProjectResource.save(project).$promise
            .then(function() {
              SimpleMessageToastService.openSimpleMessageToast(
                i18nPrefix + 'released-beta-successfully', {
                  id: project.id
                });
              CurrentProjectService.setCurrentProject(project);
            });
        } else {
          //REGULAR RELEASE
          release.date = new Date().toISOString();
          project.release = release;
          DaraReleaseResource.release(project)
            .$promise.then(function() {
            project.hasBeenReleasedBefore = true;
            DataAcquisitionProjectResource.save(project).$promise
              .then(function() {
                SimpleMessageToastService.openSimpleMessageToast(
                  i18nPrefix + 'released-successfully', {
                    id: project.id
                  });
                CurrentProjectService.setCurrentProject(project);
              });
          }).catch(function() {
            delete project.release;
            SimpleMessageToastService.openSimpleMessageToast(
              i18nPrefix + 'dara-released-not-successfully', {
                id: project.id
              });
          });
        }
      }

      $mdDialog.hide();
    };
  });
