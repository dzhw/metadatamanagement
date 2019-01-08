'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitUserlist', function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'project-cockpit-userlist.html.tmpl',
      scope: true,
      link: function(scope, elem, attrs) { // jshint ignore:line
        scope.group = attrs.group;
        scope.disabled = !scope.advancedPrivileges &&
          attrs.group === 'publishers';
      }
    };
  });
