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
                    if (release.toTweet && release.version) {
                      // create the doi link like it is created in backend {DoiBuilder.java}
                      // (requesting doi through search service is not possible because 
                      // for new projects the doi link would not be available immediately
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

