(function() {
  'use strict';

  var Component = {
    controller: 'editScriptSectionController',
    templateUrl: 'scripts/analysispackagemanagement/' +
      'components/scriptsection/edit-script-section.html.tmpl',
    bindings: {
      currentForm: '<',
      packageId: '<',
      scripts: '='
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('editScriptSectionComponent', Component);
})();
