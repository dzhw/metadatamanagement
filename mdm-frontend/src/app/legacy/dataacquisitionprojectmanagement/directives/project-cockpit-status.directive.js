/* global _ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitStatus', ['SearchDao', 'ProjectUpdateAccessService', 'SimpleMessageToastService', 'Principal', 'DataAcquisitionProjectPostValidationService', '$mdDialog', '$translate', 'ProjectReleaseService', '$q',  function(
    SearchDao, ProjectUpdateAccessService,
    SimpleMessageToastService, Principal,
    DataAcquisitionProjectPostValidationService,
    $mdDialog, $translate,
    ProjectReleaseService, $q) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'project-cockpit-status.html.tmpl',
      scope: {
        project: '='
      },
      replace: true,
      controllerAs: 'ctrl',
      controller: ['$scope', function($scope) {
        this.project = $scope.project;
        this.isAssignedDataProvider =
          ProjectUpdateAccessService.isAssignedToProject.bind(null,
            this.project, 'dataProviders');
        this.isAssignedPublisher =
          ProjectUpdateAccessService.isAssignedToProject.bind(null,
            this.project, 'publishers');
      }],
      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl) {

        var sortByRequiredState = function() {
          var optionalStates = ['surveys', 'instruments', 'questions',
            'dataSets', 'variables', 'publications', 'concepts', 'fake1'];
          var activeStates = _.filter(optionalStates, function(state) {
            return ctrl.project.configuration.requirements[state + 'Required'];
          });
          var inactiveStates = _.difference(optionalStates, activeStates);
          return activeStates.concat(inactiveStates);
        };

        ctrl.sortedStates = sortByRequiredState();

        $scope.$on('project-changed', function() {
          ctrl.sortedStates = sortByRequiredState();
        });

        ctrl.getNextAssigneeGroup = function(project) {
          switch (_.get(project, 'assigneeGroup')) {
            case 'DATA_PROVIDER':
              return 'PUBLISHER';
            case 'PUBLISHER':
              return 'DATA_PROVIDER';
            default:
              return '';
          }
        };

        var showAssigneeGroupMessageDialog = function(recipient) {
          var assigneeGroup = _.get(ctrl.project, 'assigneeGroup');
          recipient = recipient || assigneeGroup;

          switch (recipient) {
            case 'PUBLISHER':
              recipient = $translate.instant('data-acquisition' +
                '-project-management.project-cockpit.label.ROLE_DATA_PROVIDER');
              break;
            case 'DATA_PROVIDER':
              recipient = $translate.instant('data-acquisition' +
                '-project-management.project-cockpit.label.ROLE_PUBLISHER');
              break;
            default:
              recipient = '';
          }

          return $mdDialog.show({
            controller: 'AssigneeMessageDialogController',
            templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
              'assignee-message-dialog.html.tmpl',
            fullscreen: true,
            locals: {
              recipient: recipient
            }
          });
        };

        ctrl.onSaveChangesAndTakeBack = function() {
          var confirm = $mdDialog.confirm()
            .title($translate.instant('data-acquisition' +
              '-project-management.project-cockpit.takeback-dialog.title')
            ).textContent($translate.instant('data-acquisition' +
              '-project-management.project-cockpit.takeback-dialog.text')
            ).ok($translate.instant('global.common-dialogs.yes'))
            .cancel($translate.instant('global.common-dialogs.no'));
          $mdDialog.show(confirm).then(function() {
            showAssigneeGroupMessageDialog('PUBLISHER')
            .then(function(message) {
              ctrl.project.lastAssigneeGroupMessage = message;
              ctrl.project.assigneeGroup = 'PUBLISHER';
            });
          });
        };

        ctrl.onSaveChangesAndAssign = function() {
          if (!_.get(ctrl.project, 'configuration.dataProviders.length')) {
            SimpleMessageToastService
            .openAlertMessageToast('data-acquisition' +
              '-project-management.project-cockpit.no-data-providers' +
              '-dialog.text');
            return;
          }
          var postValidationStep = $q.defer();
          var newAssigneeGroup = ctrl.getNextAssigneeGroup(ctrl.project);
          var isPublisher = Principal.hasAuthority('ROLE_PUBLISHER');

          if (newAssigneeGroup === 'PUBLISHER' && !isPublisher) {
            DataAcquisitionProjectPostValidationService
              .postValidate(ctrl.project.id)
              .then(postValidationStep.resolve, postValidationStep.reject);
          } else {
            postValidationStep.resolve();
          }
          postValidationStep.promise.then(function() {
            showAssigneeGroupMessageDialog().then(function(message) {
              ctrl.project.lastAssigneeGroupMessage = message;
              ctrl.project.assigneeGroup = newAssigneeGroup;
            });
          });
        };

        ctrl.counts = {};
        var setTypeCounts = function(projectId) {
          SearchDao.search('', 1, projectId, {}, undefined, 0, undefined)
            .then(function(data) {
              ['variables', 'questions', 'data_sets', 'surveys', 'instruments',
                'data_packages', 'analysis_packages', 'related_publications',
                'concepts'].forEach(
              function(type) {
                var bucket = _.find(data.aggregations.countByType.buckets,
                  {key: type});
                ctrl.counts[type] = _.get(bucket, 'doc_count', 0);
              });
            });
        };
        setTypeCounts(ctrl.project.id);

        $scope.$on('upload-completed', function() {
          setTypeCounts(ctrl.project.id);
        });

        $scope.$on('deletion-completed', function() {
          setTypeCounts(ctrl.project.id);
        });

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

        /**
         * Method to check wether there is an embargo date 
         * and wether this date has expired.
         */
        ctrl.isEmbargoDateExpired = function() {
          if (ctrl.project.embargoDate) {
            var current = new Date();
            return new Date(ctrl.project.embargoDate) < current;
          }
          return true;
        }

        /**
         * Checks whether a project is fully released. Full releases are indicated
         * with a release object being present but the 'isPreRelease' attribute being set to false.
         * @returns true if the project is fully released else false
         */
        ctrl.isFullyReleased = function() {
          return ctrl.project.release && !ctrl.project.release.isPreRelease ? true : false;
        }

        /**
         * Checks whether a project is pre-released. Pre-releases are indicated
         * with a release object being present but the 'isPreRelease' attribute being set to true.
         * @returns true if the project is pre-released else false
         */
        ctrl.isPreReleased = function() {
          return ctrl.project.release && ctrl.project.release.isPreRelease ? true : false;
        }

        // Methods for displaying buttons and tooltips ///////////////////

        /**
         * Checks whether the tooltip for disallowed user actions should be shown.
         * Only assigned publishers are allowed to release projects. Releases are only allowed if 
         * the project is assigned to the publisher group.
         * @returns true if the user is allowed else false
         */
        ctrl.shouldDisplayUserNotAllowedTooltip = function() {
          return !ctrl.isAssignedPublisher() || ctrl.project.assigneeGroup !== 'PUBLISHER'
        };

        /**
         * Checks whether the tooltip for regular releases should be shown.
         * Regular releases are allowed if the the project is currently not released and there is no embargo date or the
         * embargo date has expired. It is also allowed if the project is currently released but in a pre-release and the
         * embargo date has expired.
         * @returns 
         */
        ctrl.shouldDisplayReleaseTooltip = function() {
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
        ctrl.shouldDisplayPreReleaseTooltip = function() {
          return !ctrl.project.release && !ctrl.isEmbargoDateExpired();
        };

        /**
         * Checks whether the tooltip for disallowed release should be shown.
         * Releases are not allowed if the project is currenlty pre-released and the mebargo date has not expired yet.
         * @returns 
         */
        ctrl.shouldDisplayReleaseNotAllowedTooltip = function() {
          return ctrl.project.release && ctrl.project.release.isPreRelease && !ctrl.isEmbargoDateExpired();
        };

        /**
         * Checks whether the tooltip for unrelease action should be shown.
         * Unreleases can only be triggert if the project is fully released.
         * @returns 
         */
        ctrl.shouldDisplayUnreleaseTooltip = function() {
          return ctrl.project.release && !ctrl.project.release.isPreRelease;
        };

        /**
         * Checks whether the pre-release icon should be shown.
         * Pre-Releases are only possible if the project is currently not released and the embargo date is set and has not expired.
         */
        ctrl.shouldDisplayPreReleaseIcon = function() {
          return !ctrl.project.release && !ctrl.isEmbargoDateExpired();
        }

        /**
         * Checks whether the regular release icon should be shown.
         * Regular releases are possible if the project is currently not released and there is no embargo date or the embargo date has expired.
         * Regular releases are also possible if the project is currently pre-released.
         * @returns 
         */
        ctrl.shouldDisplayRegularReleaseIcon = function() {
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
        ctrl.shouldDisplayUnreleaseIcon = function() {
          return ctrl.project.release && !ctrl.project.release.isPreRelease;
        }


      }
    };
  }]);

