'use strict';

/**
 * Controller handling project releases
 */
angular.module('metadatamanagementApp')
  .controller('ReleaseProjectDialogController', [
  '$scope',
  '$mdDialog',
  'project',
  'SimpleMessageToastService',
  'DataAcquisitionProjectResource',
  'DaraReleaseResource',
  '$rootScope',
  'CurrentProjectService',
  'DataAcquisitionProjectLastReleaseResource',
  '$state',
  '$translate',
  'DataAcquisitionProjectPostValidationService',
  'PinnedDataPackagesService',
  'DataPackageIdBuilderService',
  'ENV', function($scope, $mdDialog,
    project, SimpleMessageToastService, DataAcquisitionProjectResource,
    DaraReleaseResource, $rootScope, CurrentProjectService,
    DataAcquisitionProjectLastReleaseResource, $state, $translate,
    DataAcquisitionProjectPostValidationService, PinnedDataPackagesService,
    DataPackageIdBuilderService, ENV) {
    $scope.bowser = $rootScope.bowser;
    $scope.project = project;
    $scope.ENV = ENV;
    $scope.embargoString = $scope.project.embargoDate ? new Date($scope.project.embargoDate).toLocaleDateString('de-DE', {day:'2-digit', month:'2-digit', year:'numeric'}) : '';

    var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
      'data-acquisition-project.';
    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    DataAcquisitionProjectLastReleaseResource.get({id: project.id})
    .$promise.then(function(lastRelease) {
      if (lastRelease.version) {
        $scope.lastVersion = lastRelease.version;
        $scope.release = {};
        $scope.release.version = lastRelease.version;
        $scope.release.isPreRelease = lastRelease.isPreRelease;
        $scope.release.doiPageLanguage = lastRelease.doiPageLanguage;
        PinnedDataPackagesService.getPinnedDataPackage().then(
          function(response) {
            if (response.data && response.data.id ===
              DataPackageIdBuilderService.buildDataPackageId(project.id,
                lastRelease.version)) {
              $scope.release.pinToStartPage = true;
            } else {
              $scope.release.pinToStartPage = false;
            }
          });
      }
    });

    /**
     * Method handling releases. 
     * In case of a pre-release the project is validated and released 
     * with the 'isPreRelease' attribute set to true. The metadata is sent to DA|RA.
     * If it is not a pre-release the project is validated. If it is a beta release
     * with a version < 1.0.0 the project is saved with the release object but no data is send to DA|RA.
     * If it is a regular release the project is saved and the metadata is sent to DA|RA.
     * @param {*} release 
     */
    $scope.ok = function(release) {
      if ($scope.isPreRelease()) {
        // Handling for pre-releases
        DataAcquisitionProjectPostValidationService
        .postValidatePreRelease(project.id).then(function() {
          var compareForBeta = $scope.bowser
            .compareVersions(['1.0.0', release.version]);
          // early return in case of beta release
          if (compareForBeta === 1) {
            return;
          }
          release.lastDate = new Date().toISOString();
          release.isPreRelease = true;
          project.release = release;
          project.hasBeenReleasedBefore = true;
          DaraReleaseResource.preRelease(project)
          .$promise.then(function() {
              DataAcquisitionProjectResource.save(project).$promise
              .then(function() {
                  SimpleMessageToastService.openSimpleMessageToast(
                    i18nPrefix + 'released-successfully', {
                      id: project.id
                    });
                  CurrentProjectService.setCurrentProject(project);
                  $mdDialog.hide();
                  $state.forceReload();
                });
            }).catch(function() {
                delete project.release;
                SimpleMessageToastService.openAlertMessageToast(
                  i18nPrefix + 'dara-released-not-successfully', {
                    id: project.id
                  });
                $mdDialog.hide();
              });
        }).catch(function(error) {
          console.log(error)
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
      } else {
        DataAcquisitionProjectPostValidationService
          .postValidate(project.id, release.version).then(function() {
            var compareForBeta = $scope.bowser
            .compareVersions(['1.0.0', release.version]);
            release.lastDate = new Date().toISOString();
            release.isPreRelease = false;
            project.release = release;
            project.hasBeenReleasedBefore = true;
            project.embargoDate = null;
            // handling for beta releases
            if (compareForBeta === 1) {
              DataAcquisitionProjectResource.save(project).$promise
              .then(function() {
                  SimpleMessageToastService.openSimpleMessageToast(
                    i18nPrefix + 'released-beta-successfully', {
                      id: project.id
                    });
                  CurrentProjectService.setCurrentProject(project);
                  $mdDialog.hide();
                  $state.forceReload();
                }).catch(function() {
                    delete project.release;
                    SimpleMessageToastService.openAlertMessageToast(
                      i18nPrefix + 'dara-released-not-successfully', {
                        id: project.id
                      });
                      $mdDialog.hide();
                  });
            } else {
              // handling for regular releases
              DaraReleaseResource.release(project)
              .$promise.then(function() {
                  DataAcquisitionProjectResource.save(project).$promise
                  .then(function() {
                      SimpleMessageToastService.openSimpleMessageToast(
                        i18nPrefix + 'released-successfully', {
                          id: project.id
                        });
                      CurrentProjectService.setCurrentProject(project);
                      $mdDialog.hide();
                      $state.forceReload();
                    });
                }).catch(function() {
                    delete project.release;
                    SimpleMessageToastService.openAlertMessageToast(
                      i18nPrefix + 'dara-released-not-successfully', {
                        id: project.id
                      });
                    $mdDialog.hide();
                  });
            }
          }).catch(function() {
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
          })
      };
    };

    /**
     * Method to check wether the release is going to be a pre-release. If an embargo date is set
     * and has not expired it is a pre-release. Otherwise it is a regular release.
     */
    $scope.isPreRelease = function() {
      if ($scope.project.embargoDate) {
        var current = new Date();
        return new Date($scope.project.embargoDate) > current;
      }
      return false;
    };

    /**
     * Method to retrieve the next major version from the current version.
     * @returns the next major version as a string
     */
    $scope.getNextMajorVersion = function() {
      if ($scope.lastVersion != undefined) {
        var major = $scope.lastVersion.split(".")[0];
        var highestMajorVersion = +major + 1;
        return highestMajorVersion + ".0.0";
      }
      return '1.0.0';
      
    };
  }]);

