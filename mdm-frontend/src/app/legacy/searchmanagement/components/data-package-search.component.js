(function() {
  'use strict';

  var DataPackageSearchComponent = {
    bindings: {
      // items: '<',
      name: '<'
    },
    controller: 'DataPackageSearchController',
    templateUrl: 'scripts/searchmanagement/components/' +
      'data-package-search.html.tmpl'
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzDataPackageSearch', DataPackageSearchComponent);

})();
