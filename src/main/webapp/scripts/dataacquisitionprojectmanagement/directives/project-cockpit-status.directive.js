/* global _ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitStatus', function(
    SearchDao, ProjectUpdateAccessService,
    SimpleMessageToastService, Principal, ProjectSaveService,
    DataAcquisitionProjectPostValidationService,
    $mdDialog, $translate, CurrentProjectService, $timeout,
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
      controller: function($scope) {
        this.project = $scope.project;
        this.update = function() {
          this.isAssignedDataProvider =
            ProjectUpdateAccessService.isAssignedToProject(
              this.project, 'dataProviders');
          this.isAssignedPublisher =
            ProjectUpdateAccessService.isAssignedToProject(
              this.project, 'publishers');
        }.bind(this);
        this.update();
      },
      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl) {
        $scope.$on('current-project-changed', ctrl.update);

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
          var currentProject = CurrentProjectService.getCurrentProject();
          var assigneeGroup = _.get(currentProject, 'assigneeGroup');
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

        var saveProject = function(project) {
          return ProjectSaveService.saveProject(project);
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
            showAssigneeGroupMessageDialog('PUBLISHER').then(function(message) {
              var project = ProjectSaveService.prepareProjectForSave
              (ctrl.project, message, 'PUBLISHER');
              saveProject(project);
            });
          });
        };

        ctrl.onSaveChangesAndAssign = function() {
          if (!_.get(ctrl.project, 'configuration.dataProviders.length')) {
            SimpleMessageToastService.openAlertMessageToast('data-acquisition' +
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
              var project = ProjectSaveService.prepareProjectForSave(
                ctrl.project, message, newAssigneeGroup);
              saveProject(project);
            });
          });
        };

        ctrl.counts = {};
        var setTypeCounts = function(projectId) {
          SearchDao.search('', 1, projectId, {}, undefined, 0, undefined)
            .then(function(data) {
              ['variables', 'questions', 'data_sets', 'surveys', 'instruments',
                'studies'].forEach(function(type) {
                var bucket = _.find(data.aggregations.countByType.buckets,
                  {key: type});
                ctrl.counts[type] = _.get(bucket, 'doc_count', 0);
              });
            });
        };
        setTypeCounts(ctrl.project.id);

        $scope.$on('upload-completed', function() {
          $timeout(function() {
            setTypeCounts(ctrl.project.id);
          }, 2000);
        });

        $scope.$on('deletion-completed', function() {
          $timeout(function() {
            setTypeCounts(ctrl.project.id);
          }, 2000);
        });

        ctrl.toggleReleaseProject = function() {
          if (ctrl.project.release) {
            ProjectReleaseService.unreleaseProject(ctrl.project);
          } else {
            ProjectReleaseService.releaseProject(ctrl.project);
          }
        };

      }
    };
  });
