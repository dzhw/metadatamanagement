'use strict';

var SearchComponent = {
  bindings: {
    // items: '<',
    name: '<'
  },
  controller: 'SearchController',
  templateUrl: 'scripts/common/search/search.html.tmpl'
};

angular
  .module('metadatamanagementApp')
  .component('fdzSearch', SearchComponent);

