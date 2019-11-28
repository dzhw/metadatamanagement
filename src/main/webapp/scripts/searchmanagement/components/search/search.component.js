(function() {
  'use strict';

  var SearchComponent = {
    bindings: {
      // items: '<',
      name: '<'
    },
    controller: 'DataPacketSearchController',
    templateUrl: 'scripts/searchmanagement/components/search/search.html.tmpl'
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzSearch', SearchComponent);

})();
