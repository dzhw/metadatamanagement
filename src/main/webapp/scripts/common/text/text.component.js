(function() {
  'use strict';

  var Component = {
    controller: 'textController',
    bindings: {
      index: '<',
      name: '@',
      bilingual: '<',
      singleLine: '<',
      notNull: '<',
      currentForm: '<',
      translationKeyManagement: '@',
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
      if ($attrs.singleLine) {
        return 'scripts/common/text/text-single-line-bilingual.html.tmpl';
      } else {
        return tmpl[$attrs.bilingual];
      }
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('textComponent', Component);
})();
