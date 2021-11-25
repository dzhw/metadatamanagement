(function() {
  'use strict';

  var Component = {
    controller: 'listDataPackageController',
    templateUrl: 'scripts/analysispackagemanagement/' +
      'components/listdatapackage/list-data-package.html.tmpl',
    bindings: {
      currentForm: '<',
      packages: '='
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('listDataPackageComponent', Component);
})();
