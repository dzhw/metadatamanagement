'use strict';
/*global jQuery:false */
angular.module('metadatamanagementApp').service('ElasticSearchClient',
    function($rootScope) {
  var client = new jQuery.es.Client({
    hosts: $rootScope.elasticSearchProperties.url
  });
  return client;
});
