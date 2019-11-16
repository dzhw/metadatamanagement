'use strict';

var SearchComponent = {
  controller: 'searchDetailController',
  templateUrl: 'scripts/searchmanagement/components' +
              '/search-detail/search.detail.html.tmpl'
};

angular
  .module('metadatamanagementApp')
  .component('fdzSearchDetail', SearchComponent);

