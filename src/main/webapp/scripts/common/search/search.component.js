'use strict';

var SearchComponent = {
  bindings: {
    items: '<'
  },
  // TODO: Change from search-management ctrl to the component ctrl
  controller: 'SearchController',
  // controller: 'searchController',
  templateUrl: 'scripts/common/search/search.html.tmpl'
};

angular
  .module('metadatamanagementApp')
  .component('fdzSearch', SearchComponent);

