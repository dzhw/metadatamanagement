(function() {
  'use strict';

  var SearchFilterComponent = {
    controller: 'SearchFilterController',
    templateUrl: ['$attrs', function($attrs) {
      return $attrs.templateUrl;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzSearchFilter', SearchFilterComponent);
})();
