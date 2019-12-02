'use strict';

var BreadcrumbsComponent = {
  bindings: {
    items: '<'
  },
  templateUrl: 'scripts/common/breadcrumbs/breadcrumbs.html.tmpl'
};

angular
  .module('metadatamanagementApp')
  .component('fdzBreadcrumbs', BreadcrumbsComponent);
