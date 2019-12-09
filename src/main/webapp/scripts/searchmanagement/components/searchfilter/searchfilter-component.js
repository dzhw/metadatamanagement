(function() {
  'use strict';

  var SearchfilterComponent = {
    controller: 'SearchFilterController',
    templateUrl: ['$attrs', function($attrs) {
      return $attrs.templateUrl;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzSearchFilter', SearchfilterComponent);
})();
