(function() {
  'use strict';

  var SearchComponent = {
    controller: 'SearchDetailController',
    templateUrl: 'scripts/common/details/' +
      'search.detail.html.tmpl'
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzSearchDetail', SearchComponent);
})();
