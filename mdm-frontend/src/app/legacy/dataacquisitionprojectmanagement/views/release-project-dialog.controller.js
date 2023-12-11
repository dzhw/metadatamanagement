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
  'AnalysisPackageIdBuilderService',
  'DataAcquisitionProjectTweetResource',
  'DataPackageSearchService',
  'AnalysisPackageSearchService',
  'DoiService',
  'ENV', function($scope, $mdDialog,
    project, SimpleMessageToastService, DataAcquisitionProjectResource,
    DaraReleaseResource, $rootScope, CurrentProjectService,
    DataAcquisitionProjectLastReleaseResource, $state, $translate,
    DataAcquisitionProjectPostValidationService, PinnedDataPackagesService,
    DataPackageIdBuilderService, AnalysisPackageIdBuilderService,
    DataAcquisitionProjectTweetResource,
    DataPackageSearchService, AnalysisPackageSearchService, DoiService, ENV) {
    $scope.bowser = $rootScope.bowser;
    $scope.project = project;
    $scope.ENV = ENV;
    $scope.isBetaRelease = false;
    $scope.embargoString = $scope.project.embargoDate ? new Date($scope.project.embargoDate).toLocaleDateString('de-DE', {day:'2-digit', month:'2-digit', year:'numeric'}) : '';

    var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
      'data-acquisition-project.';
    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    $scope.$watch('release.toTweet', function(newValue) {
      if (!newValue && $scope.release) {
        // Set all radio buttons for tweet image to false if toTweet flag gets unchecked
        $scope.release.selectedTweetImage = null;
      }
    });

    $scope.$watch('release.version', function(newValue) {
      // Do not show tweet form fields if beta release
      if (!!newValue) {
        $scope.isBetaRelease = $scope.bowser.compareVersions(['1.0.0', newValue]) === 1;
      } else {
        $scope.isBetaRelease = false;
      }
    });

    $scope.postTweet = function(release) {
      // optional tweet about (re-)releases
      if (release.toTweet && release.version) {
        // create the doi link like it is created in backend {DoiBuilder.java}
        // (requesting doi through search service is not possible because 
        // the current doi link would not be available immediately)
        var doiLink = DoiService.createDoiLink(project.id, release.version);
        $scope.release.tweetTextInput += " " + doiLink;

        $scope.release.tweetTextInput += " (Version " + release.version + ")";
        
        DataAcquisitionProjectTweetResource.createTweet({
            tweetTextInput: $scope.release.tweetTextInput,
            selectedTweetImage: $scope.release.selectedTweetImage
        }).$promise.then(function(response) {
            console.debug("Tweet response:", response.response)
          })
          .catch(function(error) {
            console.error("Tweet error response:", error)
        });
      }
    };

    $scope.setTweetPlaceholder = function(response) {
      // set the default tweet placeholder text
      $scope.release.toTweet = false;
      $scope.release.selectedTweetImage = null;
      $scope.release.tweetTextInput = "";
      // german version
      if (response && response.title && response.title.de && response.title.de.trim() != "") {
        $scope.release.tweetTextInput += "Neue Daten wurden veröffentlicht! " + response.title.de;
      }
      // english version
      if (response && response.title && response.title.en && response.title.en != "") {
        if ($scope.release.tweetTextInput && $scope.release.tweetTextInput.trim() != "") {
          $scope.release.tweetTextInput += " / ";
        }
        $scope.release.tweetTextInput += " New data has been released! " + response.title.en;
      }
    };

    $scope.getTweetData = function() {
      // decide if it contains a data package or an analysis package
      var excludes = ['nested*','variables','questions',
        'surveys','instruments', 'relatedPublications',
        'concepts'];
      if (project.configuration.requirements.dataPackagesRequired) {
        // request the data package to get its title and doi through the master id
        DataPackageSearchService.findDataPackageById(
          DataPackageIdBuilderService.buildDataPackageId(project.id), excludes)
          .promise.then(function(response) {
            $scope.setTweetPlaceholder(response);
        });
      } else if (project.configuration.requirements.analysisPackagesRequired) {
        // request the analysis package to get its title and doi through the master id
        AnalysisPackageSearchService.findAnalysisPackageById(
          AnalysisPackageIdBuilderService.buildAnalysisPackageId(project.id), excludes)
          .promise.then(function(response) {
            $scope.setTweetPlaceholder(response);
        });
      }
    };

    DataAcquisitionProjectLastReleaseResource.get({id: project.id})
    .$promise.then(function(lastRelease) {
      $scope.release = {};
      if (lastRelease.version) {
        $scope.lastVersion = lastRelease.version;
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
          });
      }
      $scope.getTweetData();
    });

    $scope.ok = function(release) {
      console.log("releasing", release)
      if ($scope.isPreRelease()) {
        DataAcquisitionProjectPostValidationService
        .postValidatePreRelease(project.id).then(function() {
          console.log("Hier:", )
          release.lastDate = new Date().toISOString();
          release.isPreRelease = true;
          project.release = release;
          project.hasBeenReleasedBefore = true;
          console.log("Pre-RELEASE")
          //Pre RELEASE
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
<<<<<<< HEAD
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

                    $scope.postTweet(release);
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
=======
              });
        }).catch(function(error) {
          console.log(error)
>>>>>>> 4f0e4b4a97 (#3216: configured release dialog and postvalidation for pre release)
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
            // var compareForBeta = $scope.bowser
            // .compareVersions(['1.0.0', release.version]);
            release.lastDate = new Date().toISOString();
            release.isPreRelease = false;
            project.release = release;
            project.hasBeenReleasedBefore = true;

            // if (compareForBeta === 1) {
            //   console.log("This is a Beta")
              //BETA RELEASE
              // DataAcquisitionProjectResource.save(project).$promise
              // .then(function() {
              //     SimpleMessageToastService.openSimpleMessageToast(
              //       i18nPrefix + 'released-beta-successfully', {
              //         id: project.id
              //       });
              //     CurrentProjectService.setCurrentProject(project);
              //     $mdDialog.hide();
              //     $state.forceReload();
              //   }).catch(function() {
              //       delete project.release;
              //       SimpleMessageToastService.openAlertMessageToast(
              //         i18nPrefix + 'dara-released-not-successfully', {
              //           id: project.id
              //         });
              //         $mdDialog.hide();
              //     });
            // } else {
              console.log("Regular")
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
                    });
                }).catch(function() {
                    delete project.release;
                    SimpleMessageToastService.openAlertMessageToast(
                      i18nPrefix + 'dara-released-not-successfully', {
                        id: project.id
                      });
                    $mdDialog.hide();
                  });
            // }
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

