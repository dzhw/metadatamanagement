'use strict';

var SearchfilterComponent = {
  controller: 'SearchFilterController',
  templateUrl: function($attrs) {
    return $attrs.templateUrl;
  }
};

angular
  .module('metadatamanagementApp')
  .component('fdzSearchFilter', SearchfilterComponent);
