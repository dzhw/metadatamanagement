(function() {
  'use strict';

  var Component = {
    controller: 'customDataPackageController',
    templateUrl: 'scripts/analysispackagemanagement/' +
      'components/customdatapackage/custom.html.tmpl',
    bindings: {
      currentForm: '<',
      package: '=',
      index: '<'
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('customDataPackageComponent', Component);
})();
