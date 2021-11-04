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
    // templateUrl: ['$attrs', function($attrs) {
    //   var tmpl = {
    //     false: 'scripts/common/url/url.html.tmpl',
    //     true: 'scripts/common/url/url-with-index.html.tmpl'
    //   };
    //   if ($attrs.index) {
    //     return tmpl.true;
    //   }
    //   return tmpl.false;
    // }]
  };

  angular
    .module('metadatamanagementApp')
    .component('urlComponent', Component);
})();
