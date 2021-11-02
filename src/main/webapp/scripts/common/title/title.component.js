(function() {
  'use strict';

  var Component = {
    controller: 'titleController',
    templateUrl: 'scripts/common/' +
      'title/title.html.tmpl',
    bindings: {
      index: '<',
      name: '@',
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
    .component('titleComponent', Component);
})();
