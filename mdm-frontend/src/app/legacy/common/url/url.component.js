(function() {
  'use strict';

  var Component = {
    controller: 'urlController',
    bindings: {
      index: '<',
      name: '@',
      currentForm: '<',
      translationKeyPackage: '@',
      translationKeyName: '@',
      maxLength: '<',
      content: '='
    },
    templateUrl: ['$attrs', function($attrs) {
      return $attrs.templateUrl;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('urlComponent', Component);
})();
