(function() {
  'use strict';

  var Component = {
    controller: 'editScriptSectionController',
    templateUrl: 'scripts/analysispackagemanagement/' +
      'components/scriptsection/edit-script-section.html.tmpl',
    bindings: {
      currentForm: '<',
      scripts: '='
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('editScriptSectionComponent', Component);
})();
