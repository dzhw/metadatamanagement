(function() {
  'use strict';

  var BreadcrumbsComponent = {
    controller: 'BreadcrumbController',
    bindings: {
      items: '<'
    },
    templateUrl: 'scripts/common/breadcrumbs/breadcrumbs.html.tmpl'
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzBreadcrumbs', BreadcrumbsComponent);
})();
