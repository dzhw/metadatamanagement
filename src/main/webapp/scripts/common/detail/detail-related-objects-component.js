(function() {
  'use strict';

  var RelatedObjectsComponent = {
    controller: 'RelatedObjectsController',
    bindings: {
      lang: '<',
      options: '<',
      id: '<',
      collapsed: '<'
    },
    templateUrl: function($attrs) {
      return $attrs.templateUrl;
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzRelatedObjects', RelatedObjectsComponent);
})();
