/* global _, bowser */

'use strict';

angular.module('metadatamanagementApp')
  .directive('metadataTypeStateCard',
  function($state, ProjectStatusScoringService, ProjectUpdateAccessService,
      VariableUploadService, QuestionUploadService, DeleteMetadataService,
      SimpleMessageToastService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'metadata-type-state-card.html.tmpl',
      scope: {
        type: '@',
        counts: '=',
        project: '=',
      },
      replace: true,
      controllerAs: 'ctrl',

      controller: function($scope) {
        this.type = $scope.type;
        this.counts = $scope.counts;
        this.project = $scope.project;

        this.update = function(changedProject) {
          if (!changedProject) {
            return;
          }
          this.isAssignedDataProvider =
            ProjectUpdateAccessService.isAssignedToProject(
              changedProject, 'dataProviders');
          this.isAssignedPublisher =
            ProjectUpdateAccessService.isAssignedToProject(
              changedProject, 'publishers');
        }.bind(this);
        this.update(this.project);

        switch (this.type) {
          case 'studies':
            this.icon = 'assets/images/icons/study.svg';
            this.createState = 'studyCreate';
            this.searchState = this.type;
            this.tooltip = 'search-management.buttons.create-study-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
              'delete-all-studies-tooltip';
            this.limit = 1;
            break;
          case 'surveys':
            this.icon = 'assets/images/icons/survey.svg';
            this.createState = 'surveyCreate';
            this.searchState = this.type;
            this.tooltip = 'search-management.buttons.create-survey-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
              'delete-all-surveys-tooltip';
            this.editTooltip = 'search-management.buttons.' +
              'edit-surveys-tooltip';
            break;
          case 'instruments':
            this.icon = 'assets/images/icons/instrument.svg';
            this.createState = 'instrumentCreate';
            this.searchState = this.type;
            this.tooltip = 'search-management.buttons.' +
              'create-instrument-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
              'delete-all-instruments-tooltip';
            this.editTooltip = 'search-management.buttons.' +
              'edit-instruments-tooltip';
            break;
          case 'questions':
            this.icon = 'assets/images/icons/question.svg';
            this.createState = '';
            this.uploadFunction = function(files) {
              QuestionUploadService.uploadQuestions(files, this.project.id);
            }.bind(this);
            this.searchState = this.type;
            this.tooltip = 'search-management.buttons.' +
              'upload-questions-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
              'delete-all-questions-tooltip';
            break;
          case 'dataSets':
            this.icon = 'assets/images/icons/data-set.svg';
            this.createState = 'dataSetCreate';
            this.searchState = this.type;
            this.tooltip = 'search-management.buttons.' +
              'create-data-set-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
             'delete-all-data-sets-tooltip';
            this.editTooltip = 'search-management.buttons.' +
             'edit-data-sets-tooltip';
            break;
          case 'variables':
            this.icon = 'assets/images/icons/variable.svg';
            this.createState = '';
            this.uploadFunction = function(files) {
              VariableUploadService.uploadVariables(files, this.project.id);
            }.bind(this);
            this.searchState = this.type;
            this.tooltip = 'search-management.buttons.' +
              'upload-variables-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
             'delete-all-variables-tooltip';
            break;
          default:
            throw Error('wrong argument for group');
        }
        this.limit = this.limit || 0;
      },

      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl) {
        $scope.$on('current-project-changed',
          function(event, changedProject) { // jshint ignore:line
            ctrl.update(changedProject);
          });

        ctrl.getSentimentValue = function(tab) {
          return ProjectStatusScoringService
            .scoreProjectStatus(ctrl.project, tab);
        };

        ctrl.isRequired = function() {
          return _.get(ctrl, 'project.configuration.requirements.' +
            ctrl.type + 'Required');
        };

        ctrl.getModifyButtonLabel = function(group) {
          return ctrl.limit ? (ctrl.counts && ctrl.counts[
            // map camelCase to underscore_case
            group.replace(/([A-Z])/g,
              function($1) {return '_' + $1.toLowerCase();})
          ] >= ctrl.limit ? 'edit' : 'new') : 'new';
        };

        ctrl.getTooltip = function(group) {
          if (ctrl.counts && ctrl.limit &&
             ctrl.limit <= ctrl.counts[group]) {
            return ctrl.tooltip.replace('create', 'edit');
          }
          return ctrl.tooltip;
        };

        ctrl.isUpdateAllowed = function(type) {
          return !_.get(ctrl, 'project.configuration.requirements.' +
            type + 'Required');
        };

        ctrl.isUploadAllowed = function(type) {
          if (bowser.msie) {
            SimpleMessageToastService.openAlertMessageToast('global.error.' +
              'browser-not-supported');
            return false;
          }
          return ctrl.isCreationAllowed(type);
        };

        ctrl.isCreationAllowed = function(type) {
          return ProjectUpdateAccessService.isUpdateAllowed(
            ctrl.project, type, true);
        };

        ctrl.create = function() {
          if (ctrl.isCreationAllowed(ctrl.type)) {
            ProjectUpdateAccessService.isPrerequisiteFulfilled(
              ctrl.project, ctrl.type).then(function() {
                $state.go(ctrl.createState, {});
              });
          }
        };

        ctrl.delete = function() {
          DeleteMetadataService.deleteAllOfType(ctrl.project, ctrl.type);
        };
      }
    };
  });
