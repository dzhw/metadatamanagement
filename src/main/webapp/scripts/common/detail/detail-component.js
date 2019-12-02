(function() {
  'use strict';

  var DetailComponent = {
    bindings: {
      lang: '<',
      options: '<',
      collapsed: '<'
    },
    templateUrl: ['$attrs', function($attrs) {
      return $attrs.templateUrl;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzDetail', DetailComponent);
})();
