(function() {
  'use strict';

  var Component = {
    controller: 'EditCitationDetailsController',
    templateUrl: 'scripts/common/dialogs/attachment/components/' +
      'edit-citation-details.html.tmpl',
    bindings: {
      citationDetails: '=',
      currentForm: '<',
      selectedLanguage: '<',
      attachmentMetadataType: '<'
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('editCitationDetailsComponent', Component);
})();
