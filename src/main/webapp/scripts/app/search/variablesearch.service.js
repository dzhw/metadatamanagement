'use strict';

angular.module('metadatamanagementApp').service('client',
    function($rootScope, esFactory) {
  return esFactory({
    host: $rootScope.RemoteElasticSearch.url,
    api: $rootScope.RemoteElasticSearch.versionApi,
    log:  $rootScope.RemoteElasticSearch.logLevel
  });
});
