'use strict';

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
  'DataAcquisitionProjectTweetResource',
  'ENV', function($scope, $mdDialog,
    project, SimpleMessageToastService, DataAcquisitionProjectResource,
    DaraReleaseResource, $rootScope, CurrentProjectService,
    DataAcquisitionProjectLastReleaseResource, $state, $translate,
    DataAcquisitionProjectPostValidationService, PinnedDataPackagesService,
    DataPackageIdBuilderService, DataAcquisitionProjectTweetResource, ENV) {
    $scope.bowser = $rootScope.bowser;
    $scope.project = project;
    $scope.ENV = ENV;

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
        PinnedDataPackagesService.getPinnedDataPackage().then(
          function(response) {
            if (response.data && response.data.id ===
              DataPackageIdBuilderService.buildDataPackageId(project.id,
                lastRelease.version)) {
              $scope.release.pinToStartPage = true;
            } else {
              $scope.release.pinToStartPage = false;
            }

            // set the default tweet placeholder text
            $scope.release.toTweet = false;
            $scope.release.tweetTextInput = "";
            if (response.data && response.data.completeTitle && response.data.completeTitle.de 
              && response.data.completeTitle.de.trim() != "") {
              $scope.release.tweetTextInput += "Neue Daten wurden veröffentlicht! " + response.data.completeTitle.de;
            }
            if (response.data && response.data.completeTitle && response.data.completeTitle.en
              && response.data.completeTitle.en != "") {
              if ($scope.release.tweetTextInput && $scope.release.tweetTextInput.trim() != "") {
                $scope.release.tweetTextInput += " / "; 
              }
              $scope.release.tweetTextInput += " New data has been released! " + response.data.completeTitle.en; 
            }
            if (response.data.doi) {
              $scope.release.tweetTextInput += " https://doi.org/" + response.data.doi;
            }
          });
      }
    });

    $scope.ok = function(release) {
      DataAcquisitionProjectPostValidationService
        .postValidate(project.id, release.version).then(function() {
          var compareForBeta = $scope.bowser
          .compareVersions(['1.0.0', release.version]);
          release.lastDate = new Date().toISOString();
          project.release = release;
          project.hasBeenReleasedBefore = true;

          if (compareForBeta === 1) {
            //BETA RELEASE
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
            //REGULAR RELEASE
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

                    // tweet
                    if (release.version) {
                      $scope.release.tweetTextInput += " Version " + release.version;
                    }
                    console.debug("Tweet text: " + $scope.release.tweetTextInput);
                    DataAcquisitionProjectTweetResource.createTweet($scope.release.tweetTextInput)
                      .$promise.then(function(response) {
                         console.debug("Tweet response:", response.response)
                      })
                      .catch(function(error) {
                        console.error("Tweet error response:", error)
                    });
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
        });
    };
  }]);

