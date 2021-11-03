(function() {
  'use strict';

  var Component = {
    controller: 'textController',
    // templateUrl: 'scripts/common/text/text.html.tmpl',
    bindings: {
      index: '<',
      name: '@',
      bilingual: '<',
      notNull: '<',
      currentForm: '<',
      translationKeyPackage: '@',
      translationKeyName: '@',
      maxLength: '<',
      content: '='
    },
    templateUrl: ['$attrs' ,function($attrs) {
      var tmpl = {
        false: 'scripts/common/text/text.html.tmpl',
        true: 'scripts/common/text/text-bilingual.html.tmpl'
      };
      return tmpl[$attrs.bilingual];
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('textComponent', Component);
})();
