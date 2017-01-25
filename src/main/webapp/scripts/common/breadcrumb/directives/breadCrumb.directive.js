'use strict';

angular.module('metadatamanagementApp').directive('breadCrumb',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/common/breadcrumb/directives/breadCrumb.html.tmpl',
      scope: {
        items: '='
      }
    };
  });
