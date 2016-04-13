'use strict';
angular.module('metadatamanagementApp').service('ElasticSearchClientService',
function(esFactory, ElasticSearchProperties, $location) {
  return esFactory({
    host: $location.protocol() + '://' + $location.host() + ':' +
    $location.port() + '/api/search',
    apiVersion: ElasticSearchProperties.apiVersion,
    log: ElasticSearchProperties.logLevel
  });
});
