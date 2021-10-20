//  fdzTextAreaComponent handles sections of mono-/bilingual text areas.
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
    controller: 'fdzTextAreaController',
    templateUrl: 'scripts/common/fdztextarea/fdz-text-area.html.tmpl',
    bindings: {
      bilingual: '<',
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
    .component('fdzTextAreaComponent', Component);
})();
