'use strict';
angular.module('metadatamanagementApp').service('ElasticSearchClient', function
(esFactory, $rootScope, $location) {
  return esFactory({
    host: $location.protocol() + '://' + $location.host() + ':' +
    $location.port() + '/api/search',
    apiVersion: $rootScope.elasticSearchProperties.versionApi,
    log: $rootScope.elasticSearchProperties.logLevel
  });
});
