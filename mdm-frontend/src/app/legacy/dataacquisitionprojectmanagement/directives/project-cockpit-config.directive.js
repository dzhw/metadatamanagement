/* global _ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitConfig', [
    'Principal', 
    'DataAcquisitionProjectLastReleaseResource',
    'DataAcquisitionProjectPostValidationService',
    'DaraReleaseResource',
    'DataAcquisitionProjectResource',
    '$mdDialog',
    'SimpleMessageToastService',
    'CommonDialogsService',
    function(Principal, DataAcquisitionProjectLastReleaseResource, 
      DataAcquisitionProjectPostValidationService, DaraReleaseResource, DataAcquisitionProjectResource, 
      $mdDialog, SimpleMessageToastService, CommonDialogsService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'project-cockpit-config.html.tmpl',
      scope: {
        project: '='
      },
      replace: true,
      controllerAs: 'ctrl',
      controller: ['$scope', '$rootScope', function($scope, $rootScope) {
        this.project = $scope.project;
        this.bowser = $rootScope.bowser;
      }],
      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl) {
        var req = ctrl.project.configuration.requirements;
        var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
          'data-acquisition-project.';

        ctrl.selectedEmbargoDate = ctrl.project.embargoDate;

        // Gathering the last release to determine of the embargo date field should be disabled 
        DataAcquisitionProjectLastReleaseResource.get({id: $scope.project.id})
        .$promise.then(function(lastRelease) {
          if (lastRelease && lastRelease.version) {
            $scope.lastVersion = lastRelease.version;
            if ($scope.lastVersion) {
              ctrl.disableEmbargoDate = ctrl.bowser
                  .compareVersions([$scope.lastVersion, "1.0.0"]) === 1;
            }
            if (!$scope.lastVersion){
              ctrl.disableEmbargoDate = true;
            }
  
            ctrl.disableEmbargoDate = $scope.project.release ? true : false;
          } else {
            ctrl.disableEmbargoDate = true;
          }
        })

        /**
         * Whether the current user is not an assigned publisher of the project.
         * @returns true if the user is not an assigned publisher else false
         */
        var isNotAssignedPublisher = function() {
          return ctrl.project.configuration.publishers.indexOf(Principal.loginName()) === -1;
        };

        /**
         * Whether the current user is not an assigned data provider of the project.
         * @returns true if the user is not an assigned data provider else false
         */
        var isNotAssignedDataprovider = function() {
          return ctrl.project.configuration.dataProviders.indexOf(Principal.loginName()) === -1;
        };

        /**
         * Whether the project is released including pre-releases.
         * @returns true if the project is released or pre-released
         */
        var isProjectReleased = function() {
          return $scope.project.release ? true : false;
        };

        /**
         * Whether the embargo date field should be disabled or not.
         * @returns true if user is not assigned to the project or the project is released
         */
        ctrl.isEmbargoDateDisabled = function() {
          return (isNotAssignedPublisher() && isNotAssignedDataprovider()) || (isProjectReleased() && !$scope.project.release.isPreRelease);
        }

        /**
         * Whether the project requirements are disabled
         * @returns true if the user is not an assigned publisher or the project is fully released
         */
        ctrl.isProjectRequirementsDisabled = function() {
          return isNotAssignedPublisher() || (isProjectReleased() && !$scope.project.release.isPreRelease);
        };

        /**
         * Whether the user has the PUBLISHER role.
         * @returns 
         */
        ctrl.isUserPublisher = function() {
          return Principal.hasAuthority('ROLE_PUBLISHER');
        };

        /**
         * Sets basic requirements in case of analysis packages being selected as the project type.
         */
        ctrl.isAnalysisPackageChecked = function() {
          req.isAnalysisPackagesRequired = true;
          req.isDataPackagesRequired = false;
          req.isSurveysRequired = false;
          req.isInstrumentsRequired = false;
          req.isQuestionsRequired = false;
          req.isDataSetsRequired = false;
          req.isVariablesRequired = false;
          req.isPublicationsRequired = true;
          req.isConceptsRequired = false;
        };

        /**
         * Sets basic requirements in case of data packages being selected as the project type.
         */
        ctrl.isDataPackageChecked = function() {
          req.isAnalysisPackagesRequired = false;
          req.isDataPackagesRequired = true;
          req.isPublicationsRequired = false;
        };

        /**
         * Resets embargo date and updates Da|ra.
         * Changes on embargo date always need to be confirmed.
         */
        ctrl.removeEmbargoDate = function() {
          CommonDialogsService.showConfirmDialog(
            'global.common-dialogs' +
            '.confirm-edit-embargo-date.title',
            {},
            'global.common-dialogs' +
            '.confirm-edit-embargo-date.content',
            {},
            null
          ).then(function success() {
            const oldEmbargo = ctrl.project.embargoDate;
            ctrl.project.embargoDate = null;
            ctrl.selectedEmbargoDate = null;
            if (isProjectReleased() && $scope.project.release.isPreRelease) {
              // Handling for pre-releases
              DataAcquisitionProjectPostValidationService
              .postValidatePreRelease(ctrl.project.id).then(function() {
                var compareForBeta = ctrl.bowser
                  .compareVersions(['1.0.0', ctrl.project.release.version]);
                // early return in case of beta release
                if (compareForBeta === 1) {
                  return;
                }
                DaraReleaseResource.preRelease(ctrl.project)
                .$promise.then(function() {
                    SimpleMessageToastService.openSimpleMessageToast(
                      i18nPrefix + 'dara-update-successfully', {
                        id: ctrl.project.id
                      });
                  }).catch(function(error) {
                      console.error("Error while releasing project to Da|ra", error)
                      ctrl.project.embargoDate = oldEmbargo;
                      ctrl.selectedEmbargoDate = oldEmbargo;
                      SimpleMessageToastService.openAlertMessageToast(
                        i18nPrefix + 'dara-released-not-successfully', {
                          id: ctrl.project.id
                        });
                    });
              }).catch(function(error) {
                console.error("Release not possible:", error);
                ctrl.project.release.embargoDate = oldEmbargo;
                ctrl.selectedEmbargoDate = oldEmbargo;
                $mdDialog.show($mdDialog.alert()
                .title($translate.instant(
                  i18nPrefix + 'release-not-possible-title', {
                    id: ctrl.project.id
                  }))
                .textContent($translate.instant(
                  i18nPrefix + 'release-not-possible', {
                    id: ctrl.project.id
                  }))
                .ariaLabel($translate.instant(
                  i18nPrefix + 'release-not-possible-title', {
                    id: ctrl.project.id
                  }))
                .ok($translate.instant('global.buttons.ok')));
              });
            } else if (!isProjectReleased()) {
              ctrl.project.embargoDate = null;
              ctrl.selectedEmbargoDate = null;
            }
          }, function error() {
            ctrl.selectedEmbargoDate = ctrl.project.embargoDate;
          }) 
        }

        /**
         * Adds 3 hours to the date object to prevent daylight saving time changes from changing the date.
         * If the project is currently pre-released Da|ra is updated. Changes on embargo date always need to be confirmed.
         */
        ctrl.onEmbargoDateChanged = function() {
          CommonDialogsService.showConfirmDialog(
            'global.common-dialogs' +
            '.confirm-edit-embargo-date.title',
            {},
            'global.common-dialogs' +
            '.confirm-edit-embargo-date.content',
            {},
            null
          ).then(function success() {
            var embargoDate = new Date(ctrl.selectedEmbargoDate);
            embargoDate.setHours(3)
            ctrl.project.embargoDate = embargoDate;
            if (isProjectReleased() && $scope.project.release.isPreRelease) {
              // Handling for pre-releases
              DataAcquisitionProjectPostValidationService
              .postValidatePreRelease(ctrl.project.id).then(function() {
                var compareForBeta = ctrl.bowser
                  .compareVersions(['1.0.0', ctrl.project.release.version]);
                // early return in case of beta release
                if (compareForBeta === 1) {
                  return;
                }
                DaraReleaseResource.preRelease(ctrl.project)
                .$promise.then(function() {
                    SimpleMessageToastService.openSimpleMessageToast(
                      i18nPrefix + 'dara-update-successfully', {
                        id: ctrl.project.id
                      });
                  }).catch(function(error) {
                      ctrl.selectedEmbargoDate = ctrl.project.embargoDate;
                      console.error("Error while releasing project to Da|ra", error);
                      SimpleMessageToastService.openAlertMessageToast(
                        i18nPrefix + 'dara-released-not-successfully', {
                          id: ctrl.project.id
                        });
                    });
              }).catch(function(error) {
                ctrl.selectedEmbargoDate = ctrl.project.embargoDate;
                console.error("Release not possible:", error);
                $mdDialog.show($mdDialog.alert()
                .title($translate.instant(
                  i18nPrefix + 'release-not-possible-title', {
                    id: ctrl.project.id
                  }))
                .textContent($translate.instant(
                  i18nPrefix + 'release-not-possible', {
                    id: ctrl.project.id
                  }))
                .ariaLabel($translate.instant(
                  i18nPrefix + 'release-not-possible-title', {
                    id: ctrl.project.id
                  }))
                .ok($translate.instant('global.buttons.ok')));
              });
            }  
          }, function error() {
            ctrl.selectedEmbargoDate = ctrl.project.embargoDate;
          });        
        }
      }
    };
  }]);

