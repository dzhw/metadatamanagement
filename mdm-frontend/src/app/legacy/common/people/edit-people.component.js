(function() {
  'use strict';

  var Component = {
    controller: 'EditPeopleController',
    templateUrl: 'scripts/common/people/' +
      'edit-people.html.tmpl',
    bindings: {
      people: '=',
      peopleId: '<',
      currentForm: '<',
      translationKeys: '<',
      attachmentMetadataType: '<'
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('editPeopleComponent', Component);
})();
