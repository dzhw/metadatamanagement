'use strict';

var SearchComponent = {
  controller: 'searchDetailController',
  templateUrl: 'scripts/common/search-detail/search.detail.html.tmpl'
};

angular
  .module('metadatamanagementApp')
  .component('fdzSearchDetail', SearchComponent);

