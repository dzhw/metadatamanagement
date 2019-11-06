'use strict';

var DetailComponent = {
  controller: 'DetailController',
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
  .component('fdzDetail', DetailComponent);
