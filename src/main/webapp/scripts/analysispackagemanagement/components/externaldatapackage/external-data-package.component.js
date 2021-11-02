(function() {
  'use strict';

  var Component = {
    controller: 'externalDataPackageController',
    templateUrl: 'scripts/analysispackagemanagement/' +
      'components/externaldatapackage/external.html.tmpl',
    bindings: {
      currentForm: '<',
      package: '=',
      index: '<'
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('externalDataPackageComponent', Component);
})();
