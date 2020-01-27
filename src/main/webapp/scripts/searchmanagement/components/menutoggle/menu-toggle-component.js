(function() {
  'use strict';

  var Component = {
    controller: 'MenuToggleController',
    require: {
      parent: '^fdzSearchFilter'
    },
    bindings: {
      options: '<',
      name: '<',
      property: '<',
      collapsed: '<'
    },
    templateUrl: ['$attrs', function($attrs) {
      return $attrs.templateUrl;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzMenuToggle', Component);
})();
