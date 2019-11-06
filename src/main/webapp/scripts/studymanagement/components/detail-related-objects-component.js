'use strict';

var RelatedObjectsComponent = {
  controller: 'RelatedObjectsController',
  // controller: 'SearchController',
  bindings: {
    lang: '<',
    options: '<',
    collapsed: '<'
  },
  templateUrl: function($attrs) {
    return $attrs.templateUrl;
  }
};

angular
  .module('metadatamanagementApp')
  .component('fdzRelatedObjects', RelatedObjectsComponent);
