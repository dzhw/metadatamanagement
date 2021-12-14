//  title Component
//
//  Component bindings:
//  index                 boolean   Used for naming and switch between templates
//                                   template
//  name                  string    Used for naming the form controls:
//  currentForm            form
//  translationKeyPackage  string    eg. analysis-package
//  translationKeyName     string    eg. license
//  maxLength              integer
//  content                model     eg. ctrl.analysisPackage.example = ''
//                                   or for bilingual:
//                                   ctrl.analysisPackage.example = {
//                                     de: '',
//                                     en: ''
//                                   }
//
//                                  content must be declared in the parent
//                                  controller

(function() {
  'use strict';

  var Component = {
    controller: 'titleController',
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
      var tmpl = {
        false: 'scripts/common/title/title.html.tmpl',
        true: 'scripts/common/title/title-with-index.html.tmpl'
      };
      if ($attrs.index) {
        return tmpl.true;
      }
      return tmpl.false;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('titleComponent', Component);
})();
