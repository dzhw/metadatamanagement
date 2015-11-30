'use strict';
/*global jQuery:false */
angular.module('metadatamanagementApp').service('ElasticSearchClient',
    function($rootScope) {
  var url = 'https://public:bx6hbibdskm7j1ag4v6pyvvlsdxk';
  var url2 = 'nfhu@dwalin-us-east-1.searchly.com';
  var client = new jQuery.es.Client({
    hosts: url + url2,
    //$rootScope.elasticSearchProperties.url
    url: $rootScope.elasticSearchProperties.url
  });
  return client;
});
