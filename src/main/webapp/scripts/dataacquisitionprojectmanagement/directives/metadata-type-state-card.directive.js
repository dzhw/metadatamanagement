/* global _ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('metadataTypeStateCard',
  function($state, ProjectStatusScoringService, ProjectUpdateAccessService,
      VariableUploadService, QuestionUploadService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'metadata-type-state-card.html.tmpl',
      scope: {
        group: '@',
        counts: '<',
        project: '=',
        isAssignedPublisher: '<',
        isAssignedDataProvider: '<',
      },
      replace: true,
      transclude: true,
      controllerAs: 'ctrl',

      controller: function($scope) {
        this.group = $scope.group;
        this.counts = $scope.counts;
        this.project = $scope.project;
        this.isAssignedDataProvider = $scope.isAssignedDataProvider;
        this.isAssignedPublisher = $scope.isAssignedPublisher;

        var iconpath = 'assets/images/icons/';
        switch (this.group) {
          case 'studies':
            this.icon = iconpath + 'study.svg';
            this.createState = 'studyCreate';
            this.searchState = this.group;
            this.tooltip = 'search-management.buttons.create-study-tooltip';
            this.limit = 1;
            break;
          case 'surveys':
            this.icon = iconpath + 'survey.svg';
            this.createState = 'surveyCreate';
            this.searchState = this.group;
            this.tooltip = 'search-management.buttons.create-survey-tooltip';
            break;
          case 'instruments':
            this.icon = iconpath + 'instrument.svg';
            this.createState = 'instrumentCreate';
            this.searchState = this.group;
            this.tooltip = 'search-management.buttons.' +
              'create-instrument-tooltip';
            break;
          case 'questions':
            this.icon = iconpath + 'question.svg';
            this.createState = '';
            this.uploadFunction = function(files) {
              QuestionUploadService.uploadQuestions(files, this.project.id);
            };
            this.searchState = this.group;
            this.tooltip = 'search-management.buttons.' +
              'upload-questions-tooltip';
            break;
          case 'dataSets':
            this.icon = iconpath + 'data-set.svg';
            this.createState = 'studyCreate';
            this.searchState = this.group;
            this.tooltip = 'search-management.buttons.' +
              'create-data-set-tooltip';
            break;
          case 'variables':
            this.icon = iconpath + 'variable.svg';
            this.createState = '';
            this.uploadFunction = function(files) {
              VariableUploadService.uploadVariables(files, this.project.id);
            };
            this.searchState = this.group;
            this.tooltip = 'search-management.buttons.' +
              'upload-variables-tooltip';
            break;
          default:
            throw Error('wrong argument for group');
        }
        this.limit = this.limit || 0;
      },

      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl, $transclude) {
        $transclude(function(transclusion) {
          ctrl.hasTranscludedContent = transclusion.length > 0;
        });

        ctrl.getSentimentValue = function(tab) {
          return ProjectStatusScoringService
            .scoreProjectStatus(ctrl.project, tab);
        };

        ctrl.isRequired = function() {
          return _.get(ctrl, 'project.configuration.requirements.' +
            ctrl.group + 'Required');
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

        ctrl.create = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(ctrl.project,
            // map camelCase to underscore_case
            ctrl.group.replace(/([A-Z])/g,
              function($1) {return '_' + $1.toLowerCase();}), true)) {
            $state.go(this.createState, {});
          }
        };

      }
    };
  });
