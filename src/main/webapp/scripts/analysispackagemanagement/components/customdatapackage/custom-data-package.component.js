(function() {
  'use strict';

  var Component = {
    controller: 'customDataPackageController',
    templateUrl: 'scripts/analysispackagemanagement/' +
      'components/customdatapackage/custom.html.tmpl',
    bindings: {
      currentForm: '<',
      package: '='
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('customDataPackageComponent', Component);
})();
