(function() {
  'use strict';

  var Component = {
    bindings: {
      collapsed: '<',
      title: '<',
      options: '<',
      lang: '<'
    },
    templateUrl: function($attrs) {
      return $attrs.templateUrl;
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzAttachment', Component);

})();
