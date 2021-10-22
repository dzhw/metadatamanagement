//  editTestSection Component
//
//  Component bindings:
//  bilingual              boolean   Switch between monolingual and bilingual
//                                   template
//  notNull                boolean   Adds fdzRequired and error
//                                     validation
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
    controller: 'editTextSectionController',
    bindings: {
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
        false: 'scripts/common/textsection/edit-text-section.html.tmpl',
        true: 'scripts/common/textsection/edit-text-section' +
               '.bilingual.html.tmpl'
      };
      return tmpl[$attrs.bilingual];
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('editTextSectionComponent', Component);
})();
