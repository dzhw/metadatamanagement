'use strict';
angular.module('metadatamanagementApp').service('ElasticSearchClient',
  function(esFactory, ElasticSearchProperties, $location) {
    return esFactory({
      host: {
        protocol: $location.protocol(),
        host: $location.host(),
        port: $location.port(),
        path: '/api/search'
      },
      apiVersion: ElasticSearchProperties.apiVersion,
      log: ElasticSearchProperties.logLevel
    });
  });
