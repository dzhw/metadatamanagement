'use strict';
/*global jQuery:false */
angular.module('metadatamanagementApp').service('ElasticSearchClient',
    function() {
  var url1 = 'https://public:bx6hbibdskm7j1ag4v6pyvvl';
  var url2 = 'sdxknfhu@dwalin-us-east-1.searchly.com';
  var client = new jQuery.es.Client({
    hosts: url1 + url2 //$rootScope.elasticSearchProperties.url
  });
  return client;
});
