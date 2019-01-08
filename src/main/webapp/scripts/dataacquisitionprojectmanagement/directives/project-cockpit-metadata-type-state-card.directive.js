/* global _ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitMetadataTypeStateCard',
  function($state, ProjectStatusScoringService, ProjectUpdateAccessService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'project-cockpit-metadata-type-state-card.html.tmpl',
      scope: true,
      replace: true,
      transclude: true,
      controllerAs: 'ctrl',
      /* jshint -W098 */
      link: function(scope, elem, attrs, ctrl, $transclude) {

        scope.group = attrs.group;
        var iconpath = 'assets/images/icons/';
        switch(scope.group) {
          case 'studies':
            scope.icon = iconpath + 'study.svg';
            scope.createState = 'studyCreate';
            scope.searchState = scope.group;
            scope.tooltip = 'search-management.buttons.create-study-tooltip';
            scope.limit = 1;
          break;
          case 'surveys':
            scope.icon = iconpath + 'survey.svg';
            scope.createState = 'surveyCreate';
            scope.searchState = scope.group;
            scope.tooltip = 'search-management.buttons.create-survey-tooltip';
          break;
          case 'instruments':
            scope.icon = iconpath + 'instrument.svg';
            scope.createState = 'instrumentCreate';
            scope.searchState = scope.group;
            scope.tooltip = 'search-management.buttons.' +
              'create-instrument-tooltip';
          break;
          case 'questions':
            scope.icon = iconpath + 'question.svg';
            scope.createState = '';
            scope.uploadFunction = scope.uploadQuestions;
            scope.searchState = scope.group;
            scope.tooltip = 'search-management.buttons.' +
              'upload-questions-tooltip';
          break;
          case 'dataSets':
            scope.icon = iconpath + 'data-set.svg';
            scope.createState = 'studyCreate';
            scope.searchState = scope.group;
            scope.tooltip = 'search-management.buttons.' +
              'create-data-set-tooltip';
          break;
          case 'variables':
            scope.icon = iconpath + 'variable.svg';
            scope.createState = '';
            scope.uploadFunction = scope.uploadVariables;
            scope.searchState = scope.group;
            scope.tooltip = 'search-management.buttons.' +
              'upload-variables-tooltip';
          break;
          default:
            throw Error('wrong argument for group');
        }

        scope.limit = attrs.limit || scope.limit || 0;
        scope.create = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(scope.project,
            // map camelCase to underscore_case
            scope.group.replace(/([A-Z])/g,
              function($1) {return '_' + $1.toLowerCase();}), true)) {
            $state.go(scope.createState, {});
          }
        };
        scope.$watch(function() {
          var state = _.get(scope, 'project.configuration.' + attrs.group +
            'State.dataProviderReady');
          return state ? state : null;
        }, function(newVal, oldVal) {
          if (newVal !== oldVal) {
            scope.setChanged(true);
          }
        });
        scope.$watch(function() {
          var state = _.get(scope, 'project.configuration.' + attrs.group +
            'State.publisherReady');
          return state ? state : null;
        }, function(newVal, oldVal) {
          if (newVal !== oldVal) {
            scope.setChanged(true);
          }
        });

        $transclude(function(transclusion) {
          scope.hasTranscludedContent = transclusion.length > 0;
        });

        scope.getSentimentValue = function(tab) {
          return ProjectStatusScoringService
            .scoreProjectStatus(scope.project, tab);
        };

        scope.isRequired = function() {
          return _.get(scope, 'project.configuration.requirements.' +
            attrs.group + 'Required');
        };

        scope.getModifyButtonLabel = function(group) {
          return scope.limit ? (scope.counts && scope.counts[
            // map camelCase to underscore_case
            group.replace(/([A-Z])/g,
              function($1) {return '_' + $1.toLowerCase();})
          ] >= scope.limit ? 'edit' : 'new') : 'new';
        };

        scope.getTooltip = function(group) {
          if (scope.counts && scope.limit &&
             scope.limit <= scope.counts[group]) {
            return scope.tooltip.replace('create', 'edit');
          }
          return scope.tooltip;
        };
      }
    };
  });
