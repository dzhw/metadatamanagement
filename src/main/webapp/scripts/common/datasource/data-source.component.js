(function() {
  'use strict';

  var Component = {
    controller: 'dataSourceController',
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
    .component('datasourceComponent', Component);
})();
