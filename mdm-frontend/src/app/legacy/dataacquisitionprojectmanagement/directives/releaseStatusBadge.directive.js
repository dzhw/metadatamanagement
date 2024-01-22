'use strict';
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
        ctrl.isPreReleased = function() {
          return $scope.released && $scope.released.isPreRelease;
        };

        ctrl.isFullyReleased = function() {
          return $scope.released && !$scope.released.isPreRelease;
        }

        ctrl.isUnreleased = function() {
          return !$scope.released;
        }
      }
    };
  });
