/* global _ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitMetadataTypeStateCard',
  function($state, ProjectStatusScoringService, ProjectUpdateAccessService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
        'project-cockpit-metadata-type-state-card.html.tmpl',
      scope: true,
      replace: true,
      transclude: true,
      /* jshint -W098 */
      link: function(scope, elem, attrs, ctrl, $transclude) {

        scope.group = attrs.group;
        scope.searchState = attrs.searchstate;
        scope.icon = attrs.icon;
        scope.tooltip = attrs.tooltip;
        scope.buttonLabel = attrs.buttonLabel;
        scope.limit = attrs.limit || 0;
        scope.create = function() {
          if (ProjectUpdateAccessService.isUpdateAllowed(scope.project,
            // map camelCase to underscore_case
            scope.group.replace(/([A-Z])/g,
              function($1) {return '_' + $1.toLowerCase();}), true)) {
            $state.go(attrs.createstate, {});
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