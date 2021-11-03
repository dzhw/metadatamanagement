(function() {
  'use strict';

  var Component = {
    controller: 'textController',
    templateUrl: 'scripts/common/text/text.html.tmpl',
    bindings: {
      index: '<',
      name: '<',
      notNull: '<',
      currentForm: '<',
      translationKeyPackage: '@',
      translationKeyName: '@',
      maxLength: '<',
      content: '='
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('textComponent', Component);
})();
