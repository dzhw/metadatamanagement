'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitConfig', function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
        'project-cockpit-config.html.tmpl'
    };
  });
