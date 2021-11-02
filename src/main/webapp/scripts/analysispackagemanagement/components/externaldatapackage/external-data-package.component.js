(function() {
  'use strict';

  var Component = {
    controller: 'externalDataPackageController',
    templateUrl: 'scripts/analysispackagemanagement/' +
      'components/externaldatapackage/external.html.tmpl',
    bindings: {
      currentForm: '<',
      package: '='
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('externalDataPackageComponent', Component);
})();
