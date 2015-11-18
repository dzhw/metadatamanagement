'use strict';

angular.module('metadatamanagementApp').service('client',
    function($rootScope, esFactory) {
  return esFactory({
    host: $rootScope.elasticSearchProperties.url,
    api: $rootScope.elasticSearchProperties.versionapi,
    log: $rootScope.elasticSearchProperties.loglevel
  });
});
