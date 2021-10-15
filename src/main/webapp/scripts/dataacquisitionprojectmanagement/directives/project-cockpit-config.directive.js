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
      controllerAs: 'ctrl',
      controller: function($scope) {
        this.project = $scope.project;
      },
      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl) {
        var req = ctrl.project.configuration.requirements;

        var isNotAssignedPublisher = function() {
          var loginName = Principal.loginName();
          var publishers = _.get(ctrl.project, 'configuration.publishers', []);
          return publishers.indexOf(loginName) === -1;
        };

        var isProjectReleased = function() {
          return $scope.project.release;
        };

        ctrl.isProjectRequirementsDisabled = function() {
          return isNotAssignedPublisher() || isProjectReleased();
        };

        ctrl.isUserPublisher = function() {
          return Principal.hasAuthority('ROLE_PUBLISHER');
        };

        ctrl.isAnalysisPackageChecked = function() {
          req.dataPackagesRequired = false;
          req.surveysRequired = false;
          req.instrumentsRequired = false;
          req.questionsRequired = false;
          req.dataSetsRequired = false;
          req.variablesRequired = false;
          req.publicationsRequired = true;
          req.conceptsRequired = false;
        };

        ctrl.isDataPackageChecked = function() {
          req.analysisPackagesRequired = false;
          req.publicationsRequired = false;
        };
      }
    };
  });
