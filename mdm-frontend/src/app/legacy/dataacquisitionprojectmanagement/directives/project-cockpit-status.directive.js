/* global _ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitStatus', [
    'SearchDao', 
    'ProjectUpdateAccessService', 
    'SimpleMessageToastService', 
    'Principal', 
    'DataAcquisitionProjectPostValidationService', 
    '$mdDialog', 
    '$translate', 
    'ProjectReleaseService', 
    '$q',  
    function(
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
        project: '=',
        pidRegistrationEnabled: '<',
        variablesCheck: '<'
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
        this.hasAdminRole = () => Principal.isAdmin();
      }],
      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl) {

        var sortByRequiredState = function() {
          var optionalStates = ['surveys', 'instruments', 'questions',
            'dataSets', 'variables', 'publications', 'concepts', 'fake1'];
          var activeStates = _.filter(optionalStates, function(state) {
            return ctrl.project.configuration.requirements['is' + state.charAt(0).toUpperCase() + state.slice(1) + 'Required'];
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
      }
    };
  }]);

