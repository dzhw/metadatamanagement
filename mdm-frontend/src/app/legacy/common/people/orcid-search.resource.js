'use strict';

angular.module('metadatamanagementApp')
  .factory('ORCIDSearchResource', function($resource) {
    return $resource('https://pub.orcid.org/v3.0/expanded-search/?' +
      'q=family-name\::lastName+AND+given-names\::firstName', {
      firstName: '@firstName',
      lastName: '@lastName'
    }, {
      'get': {
        method: 'GET',
        headers: {
          'accept': 'application/vnd.orcid+json'
        }
      }
    });
  });
