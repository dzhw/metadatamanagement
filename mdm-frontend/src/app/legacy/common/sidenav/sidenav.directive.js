'use strict';

angular.module('metadatamanagementApp').directive('fdzSidenav',
  function() {
    return {
      replace: true,
      restrict: 'E',
      templateUrl: 'scripts/common/sidenav/sidenav.html.tmpl',
      controller: 'SidenavController'
    };
  });
