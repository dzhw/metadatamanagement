'use strict';

angular.module('metadatamanagementApp').service('client',
    function($rootScope, esFactory) {
  return esFactory({
    host: $rootScope.elasticSearchProperties.url,
    api: $rootScope.elasticSearchProperties.versionApi,
    log: $rootScope.elasticSearchProperties.logLevel
  });
});
