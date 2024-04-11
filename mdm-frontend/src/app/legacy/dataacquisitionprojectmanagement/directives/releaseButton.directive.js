/* global _, bowser */

'use strict';

/**
 * Directive for handling the display of the release button within the project management.
 */
angular.module('metadatamanagementApp')
  .directive('releaseButton', ['$state', 'ProjectReleaseService', 'ProjectUpdateAccessService', 'CurrentProjectService',
    function ($state, ProjectReleaseService, ProjectUpdateAccessService, CurrentProjectService) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
          'releaseButton.html.tmpl',
        scope: {
        },
        replace: true,
        controllerAs: 'ctrl',
        controller: [
          '$scope', '$rootScope', 'ProjectUpdateAccessService', 'CurrentProjectService',
          function ($scope, $rootScope, ProjectUpdateAccessService, CurrentProjectService ) {            
          }],

        /* jshint -W098 */
        link: function ($scope, elem, attrs, ctrl) {
          /* jshint +W098 */

          ctrl.project = CurrentProjectService.getCurrentProject();

          /**
           * Method to check if the current user is assigned to the project as a publisher
           * @returns true if the user is assigned to the project as a publisher else false
           */
          ctrl.isAssignedPublisher = function() {
            return ProjectUpdateAccessService.isAssignedToProject(ctrl.project, "publishers");
          }

          /**
           * Method to check if the current user is assigned to the project as a dataprovider
           * @returns true if the user is assigned to the project as a dataprovider else false
           */
          ctrl.isAssignedDataProvider = function() {
            return ProjectUpdateAccessService.isAssignedToProject(ctrl.project, "dataproviders");
          }

          /**
           * Listener for project changed event
           */
          $scope.$on('current-project-changed',
            function(event, project) { // jshint ignore:line
              ctrl.project = CurrentProjectService.getCurrentProject();
          });
          
          /**
           * Method to check wether there is an embargo date 
           * and wether this date has expired.
           */
          ctrl.isEmbargoDateExpired = function () {
            if (ctrl.project.embargoDate) {
              var current = new Date();
              return new Date(ctrl.project.embargoDate) < current;
            }
            return true;
          }

          /**
           * Checks whether a project is pre-released. Pre-releases are indicated
           * with a release object being present but the 'isPreRelease' attribute being set to true.
           * @returns true if the project is pre-released else false
           */
          ctrl.isPreReleased = function () {
            return ctrl.project.release && ctrl.project.release.isPreRelease ? true : false;
          }

          /**
           * Checks whether the tooltip for disallowed user actions should be shown.
           * Only assigned publishers are allowed to release projects. Releases are only allowed if 
           * the project is assigned to the publisher group.
           * @returns true if the user is allowed else false
           */
          ctrl.shouldDisplayUserNotAllowedTooltip = function () {
            return !ctrl.isAssignedPublisher() || ctrl.project.assigneeGroup !== 'PUBLISHER'
          };

          /**
           * Checks whether the tooltip for regular releases should be shown.
           * Regular releases are allowed if the the project is currently not released and there is 
           * no embargo date or the embargo date has expired. It is also allowed if the project is 
           * currently released but in a pre-release and the
           * embargo date has expired.
           * @returns 
           */
          ctrl.shouldDisplayReleaseTooltip = function () {
            if (!ctrl.isAssignedPublisher() || ctrl.project.assigneeGroup !== 'PUBLISHER') {
              return false;
            }
            if (!ctrl.project.release && ctrl.isEmbargoDateExpired()) {
              return true;
            }
            if (ctrl.project.release && ctrl.project.release.isPreRelease && ctrl.isEmbargoDateExpired()) {
              return true;
            }
            return false;
          };

          /**
           * Checks whether the tooltip for pre-releases should be shown.
           * Pre-Releases are allowed if the project is currently not released and there is an unexpired embargo date.
           * @returns 
           */
          ctrl.shouldDisplayPreReleaseTooltip = function () {
            if (!ctrl.isAssignedPublisher() || ctrl.project.assigneeGroup !== 'PUBLISHER') {
              return false;
            }
            return !ctrl.project.release && !ctrl.isEmbargoDateExpired();
          };

          /**
           * Checks whether the tooltip for disallowed release should be shown.
           * Releases are not allowed if the project is currenlty pre-released and the mebargo date has not expired yet.
           * @returns 
           */
          ctrl.shouldDisplayReleaseNotAllowedTooltip = function () {
            if (!ctrl.isAssignedPublisher() || ctrl.project.assigneeGroup !== 'PUBLISHER') {
              return false;
            }
            return ctrl.project.release && ctrl.project.release.isPreRelease && !ctrl.isEmbargoDateExpired();
          };

          /**
           * Checks whether the tooltip for unrelease action should be shown.
           * Unreleases can only be triggert if the project is fully released.
           * @returns 
           */
          ctrl.shouldDisplayUnreleaseTooltip = function () {
            if (!ctrl.isAssignedPublisher() || ctrl.project.assigneeGroup !== 'PUBLISHER') {
              return false;
            }
            return ctrl.project.release && !ctrl.project.release.isPreRelease;
          };

          /**
           * Checks whether the pre-release icon should be shown.
           * Pre-Releases are only possible if the project is currently not released 
           * and the embargo date is set and has not expired.
           */
          ctrl.shouldDisplayPreReleaseIcon = function () {
            return !ctrl.project.release && !ctrl.isEmbargoDateExpired();
          }

          /**
           * Checks whether the regular release icon should be shown.
           * Regular releases are possible if the project is currently not released 
           * and there is no embargo date or the embargo date has expired.
           * Regular releases are also possible if the project is currently pre-released.
           * @returns 
           */
          ctrl.shouldDisplayRegularReleaseIcon = function () {
            if (!ctrl.project.release && ctrl.isEmbargoDateExpired()) {
              return true;
            }
            // is pre-released and embargo date has expired
            if (ctrl.project.release && ctrl.project.release.isPreRelease) {
              return true;
            }
            return false;
          }

          /**
           * Checks whether the unrelease icon should be shown.
           * Unreleases are possible if the project is currently fully released.
           * @returns 
           */
          ctrl.shouldDisplayUnreleaseIcon = function () {
            return ctrl.project.release && !ctrl.project.release.isPreRelease;
          }

          /**
           * Method to toggle the release status of a project.
           * If the project is not released yet it will release it.
           * If the project is currently released but as a pre-release it will fully release it.
           * If the project already is released it will unrelease it.
           */
          ctrl.toggleReleaseProject = function() {
            if (ctrl.project.release) {
              if (ctrl.isPreReleased()) {
                ProjectReleaseService.releaseProject(ctrl.project);
              } else {
                ProjectReleaseService.unreleaseProject(ctrl.project);
              }
            } else {
              ProjectReleaseService.releaseProject(ctrl.project);
            }
          };

        }
      };
    }]);

