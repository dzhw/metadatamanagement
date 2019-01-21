/* global _ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitConfig', function(Principal) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'project-cockpit-config.html.tmpl',
      scope: {
        project: '='
      },
      replace: true,
      transclude: true,
      controllerAs: 'ctrl',
      controller: function($scope) {
        this.project = $scope.project;
      },
      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl, $transclude) {

        ctrl.isProjectRequirementsDisabled = function() {
          var loginName = Principal.loginName();
          var publishers = _.get(ctrl.project, 'configuration.publishers');
          var result;
          if (_.isArray(publishers)) {
            result = publishers.indexOf(loginName) === -1;
          } else {
            result = false;
          }
          return result;
        };
      }
    };
  });
