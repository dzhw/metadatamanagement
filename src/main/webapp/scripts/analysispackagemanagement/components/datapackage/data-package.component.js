(function() {
  'use strict';

  var Component = {
    controller: 'dataPackageController',
    templateUrl: 'scripts/analysispackagemanagement/' +
      'components/datapackage/data.html.tmpl',
    bindings: {
      currentForm: '<',
      package: '='
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzDataPackageComponent', Component);
})();
