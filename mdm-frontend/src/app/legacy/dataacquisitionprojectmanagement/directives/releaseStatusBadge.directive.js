'use strict';
/**
 * Directive rendering the release status badge für projects
 */
angular.module('metadatamanagementApp')
  .directive('releaseStatusBadge', function() {
    return {
      templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
        'directives/releaseStatusBadge.directive.html.tmpl',
      scope: {
        released: '='
      },
      controllerAs: 'ctrl',
      controller: ['$scope', function($scope) {
        this.project = $scope.project;
      }],
      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl) {
        /**
         * Whether the release state indicates a pre-release state.
         * @returns true if the release is a pre-release, else false
         */
        ctrl.isPreReleased = function() {
          return $scope.released && $scope.released.isPreRelease;
        };

        /**
         * Checks whether the release state indicates a full release.
         * @returns true if the release is fully released, else false
         */
        ctrl.isFullyReleased = function() {
          return $scope.released && !$scope.released.isPreRelease;
        }

        /**
         * Checks whether there is a release state. If projects have
         * no release information they are assumed to be unreleased.
         * @returns true if release exists, else false.
         */
        ctrl.isUnreleased = function() {
          return !$scope.released;
        }
      }
    };
  });
