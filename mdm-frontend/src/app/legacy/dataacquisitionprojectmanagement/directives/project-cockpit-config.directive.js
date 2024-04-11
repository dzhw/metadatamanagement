/* global _ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitConfig', [
    'Principal', 
    'DataAcquisitionProjectLastReleaseResource',  
    function(Principal, DataAcquisitionProjectLastReleaseResource) {
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
          var loginName = Principal.loginName();
          var publishers = _.get(ctrl.project, 'configuration.publishers', []);
          return publishers.indexOf(loginName) === -1;
        };

        /**
         * Whether the current user is not an assigned data provider of the project.
         * @returns true if the user is not an assigned data provider else false
         */
        var isNotAssignedDataprovider = function() {
          var loginName = Principal.loginName();
          var publishers = _.get(ctrl.project, 'configuration.dataproviders', []);
          return publishers.indexOf(loginName) === -1;
        };

        /**
         * Whether the project is released including pre-releases.
         * @returns true if the project is released or pre-released
         */
        var isProjectReleased = function() {
          return $scope.project.release;
        };

        /**
         * Whether the embargo date field should be disabled or not.
         * @returns true if user is not assigned to the project or the project is released
         */
        ctrl.isEmbargoDateDisabled = function() {
          return (isNotAssignedPublisher() && isNotAssignedDataprovider()) || isProjectReleased();
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
          req.analysisPackagesRequired = true;
          req.dataPackagesRequired = false;
          req.surveysRequired = false;
          req.instrumentsRequired = false;
          req.questionsRequired = false;
          req.dataSetsRequired = false;
          req.variablesRequired = false;
          req.publicationsRequired = true;
          req.conceptsRequired = false;
        };

        /**
         * Sets basic requirements in case of data packages being selected as the project type.
         */
        ctrl.isDataPackageChecked = function() {
          req.analysisPackagesRequired = false;
          req.dataPackagesRequired = true;
          req.publicationsRequired = false;
        };

        /**
         * Resets embargo date, e.g. on final release.
         */
        ctrl.removeEmbargoDate = function() {
          ctrl.project.embargoDate = null;
        }

        /**
         * Adds 3 hours to the date object to prevent daylight saving time changes from changing the date
         */
        ctrl.onEmbargoDateChanged = function() {
          var embargoDate = new Date(ctrl.project.embargoDate);
          embargoDate.setHours(3)
          ctrl.project.embargoDate = embargoDate;
        }
      }
    };
  }]);

