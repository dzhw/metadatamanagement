/* global _, bowser */

'use strict';

angular.module('metadatamanagementApp')
  .directive('metadataTypeStateCard',
  function($state, ProjectStatusScoringService, ProjectUpdateAccessService,
      VariableUploadService, QuestionUploadService, DeleteMetadataService,
      SimpleMessageToastService, AnalysisPackageIdBuilderService) {
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

      controller: [
          '$scope', '$rootScope', 'DataPackageIdBuilderService', 'Principal',
          function($scope, $rootScope, DataPackageIdBuilderService, Principal) {
        $scope.bowser = $rootScope.bowser;
        $scope.hasAuthority = Principal.hasAuthority;
        this.type = $scope.type;
        this.counts = $scope.counts;
        this.project = $scope.project;
        this.dataPackageId =  DataPackageIdBuilderService
          .buildDataPackageId(this.project.id);
        this.analysisPackageId = AnalysisPackageIdBuilderService
          .buildAnalysisPackageId(this.project.id);

        this.isAssignedDataProvider =
          ProjectUpdateAccessService.isAssignedToProject.bind(null,
            this.project, 'dataProviders');
        this.isAssignedPublisher =
          ProjectUpdateAccessService.isAssignedToProject.bind(null,
            this.project, 'publishers');

        switch (this.type) {
          case 'dataPackages':
            this.createState = 'dataPackageCreate';
            this.searchState = 'data_packages';
            this.tooltip = 'search-management.buttons.' +
              'create-data-package-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
              'delete-all-data-packages-tooltip';
            this.limit = 1;
            break;
          case 'analysisPackages':
            this.createState = 'analysisPackageCreate';
            this.searchState = 'analysis_packages';
            this.tooltip = 'search-management.buttons.' +
              'create-analysis-package-tooltip';
            this.editTooltip = 'search-management.buttons.' +
              'edit-analysis-package-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
              'delete-all-analysis-packages-tooltip';
            this.limit = 1;
            break;
          case 'surveys':
            this.createState = 'surveyCreate';
            this.searchState = this.type;
            this.tooltip = 'search-management.buttons.create-survey-tooltip';
            this.deleteTooltip = 'search-management.buttons.' +
              'delete-all-surveys-tooltip';
            this.editTooltip = 'search-management.buttons.' +
              'edit-surveys-tooltip';
            break;
          case 'instruments':
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
            this.createState = '';
            this.searchState = 'related_publications';
            this.tooltip = 'search-management.buttons.' +
             'edit-publications-tooltip';
            break;
          case 'concepts':
            this.icon = 'assets/images/icons/related-publication.svg';
            this.createState = 'conceptCreate';
            this.searchState = 'concepts';
            this.tooltip = 'search-management.buttons.' +
              'create-concept-tooltip';
            this.editTooltip = 'search-management.buttons.' +
             'edit-concepts-tooltip';
            break;
          case 'fake1':
            break;
          default:
            throw Error('wrong argument for group');
        }
        this.limit = this.limit || 0;
      }],

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
          if (ctrl.type === 'concepts') {
            return 'new';
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

        if (ctrl.type !== 'publications' && ctrl.type !== 'concepts') {
          ctrl.delete = function() {
            DeleteMetadataService.deleteAllOfType(ctrl.project, ctrl.type);
          };
        }

        ctrl.edit = function(type) {
          if (ProjectUpdateAccessService.isUpdateAllowed(
            ctrl.project, type, true)) {
            $state.go('search', {type: type});
          }
        };
      }
    };
  });
