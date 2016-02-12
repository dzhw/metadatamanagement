'use strict';
angular.module('metadatamanagementApp').service('ElasticSearchClient', function(
	esFactory, $location) {
  return esFactory({
    host: $location.protocol() + '://' + $location.host() + ':' +
    $location.port() + '/api/search/'
  });
});
