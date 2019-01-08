'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitStatus', function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'project-cockpit-status.html.tmpl'
    };
  });
