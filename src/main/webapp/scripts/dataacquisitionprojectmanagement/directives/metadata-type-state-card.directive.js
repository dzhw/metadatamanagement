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
        project: '='
      },
      replace: true,
      controllerAs: 'ctrl',

      controller: function($scope, $rootScope) {
        $scope.bowser = $rootScope.bowser;
        this.type = $scope.type;
        this.counts = $scope.counts;
        this.project = $scope.project;

        this.isAssignedDataProvider =
          ProjectUpdateAccessService.isAssignedToProject.bind(null,
            this.project, 'dataProviders');
        this.isAssignedPublisher =
          ProjectUpdateAccessService.isAssignedToProject.bind(null,
            this.project, 'publishers');

        switch (this.type) {
          case 'dataPackages':
            this.icon = 'assets/images/icons/data-package.svg';
            this.createState = 'dataPackageCreate';
            this.searchState = 'data_packages';
            this.tooltip = 'search-management.buttons.' +
              'create-data-package-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
              'delete-all-data-packages-tooltip';
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
            this.searchState = 'data_sets';
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
          case 'publications':
            this.icon = 'assets/images/icons/related-publication.svg';
            this.createState = 'publicationAssignment';
            this.searchState = 'related_publications';
            this.tooltip = 'search-management.buttons.' +
             'edit-publications-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
             'delete-publications-tooltip';
            break;
          case 'fake1':
            break;
          case 'fake2':
            break;
          default:
            throw Error('wrong argument for group');
        }
        this.limit = this.limit || 0;
      },

      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl) {
        /* jshint +W098 */
        ctrl.getSentimentValue = function(tab) {
          return ProjectStatusScoringService
            .scoreProjectStatus(ctrl.project, tab);
        };

        ctrl.isRequired = function() {
          return _.get(ctrl, 'project.configuration.requirements.' +
            ctrl.type + 'Required');
        };

        ctrl.getModifyButtonLabel = function(group) {
          if (ctrl.type === 'publications') {
            return 'edit';
          }
          return ctrl.limit ? (ctrl.counts && ctrl.counts[
            // map camelCase to underscore_case
            group.replace(/([A-Z])/g,
              function($1) {return '_' + $1.toLowerCase();})
          ] >= ctrl.limit ? 'edit' : 'new') : 'new';
        };

        ctrl.getTooltip = function(group) {
          if (ctrl.counts && ctrl.limit &&
             ctrl.limit <= ctrl.counts[
             // map camelCase to underscore_case
             group.replace(/([A-Z])/g,
               function($1) {return '_' + $1.toLowerCase();})]) {
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

        ctrl.isProjectReleased = function() {
          return ctrl.project.release;
        };

        ctrl.delete = function() {
          DeleteMetadataService.deleteAllOfType(ctrl.project, ctrl.type);
        };

        ctrl.edit = function(type) {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            ctrl.project, type, true)) {
            $state.go('search', {type: type});
          }
        };
      }
    };
  });
