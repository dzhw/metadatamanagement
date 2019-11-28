(function() {
  'use strict';

  var DetailComponent = {
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
})();
