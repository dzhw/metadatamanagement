'use strict';

angular.module('metadatamanagementApp').directive('breadCrumb',
  function(BreadCrumbService) {
    var link = function(scope) {
      scope.items = BreadCrumbService.getBreadCrumbItems();
    };
    return {
      restrict: 'E',
      link: link,
      templateUrl: 'scripts/common/breadcrumb/directives/breadCrumb.html.tmpl'
    };
  });
