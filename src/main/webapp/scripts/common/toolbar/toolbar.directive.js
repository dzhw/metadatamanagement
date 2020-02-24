'use strict';

angular.module('metadatamanagementApp').directive('fdzToolbar',
  function() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'scripts/common/toolbar/toolbar.html.tmpl',
      controller: 'ToolbarController'
    };
  });
