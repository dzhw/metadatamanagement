'use strict';

angular.module('metadatamanagementApp').directive('fdzNavbar',
  function() {
    return {
      replace: true,
      restrict: 'E',
      templateUrl: 'scripts/common/navbar/views/navbar.html.tmpl',
      controller: 'NavbarController'
    };
  });
