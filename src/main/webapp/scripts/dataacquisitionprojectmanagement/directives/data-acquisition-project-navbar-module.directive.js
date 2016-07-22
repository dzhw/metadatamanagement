/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .directive('projectNavbarModule', function() {
    return {
      restrict: 'E',
      controller: 'DataAcquisitionProjectNavbarController',
      controllerAs: 'ctrl',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'data-acquisition-project-navbar-module.html.tmpl'
    };
  });
