(function() {
  'use strict';

  var Component = {
    bindings: {
      collapsed: '<',
      headline: '<',
      options: '<',
      lang: '<'
    },
    templateUrl: ['$attrs', function($attrs) {
      return $attrs.templateUrl;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzAttachment', Component);

})();
