'use strict';

/* ROR Search Resource */
angular.module('metadatamanagementApp')
  .factory('RORSearchResource', ['$resource',  function($resource) {
    return $resource('https://api.ror.org/v2/organizations?query.advanced=names.value\::name', {
      name: '@name'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);

/*curl 'https://api.ror.org/v2/organizations?query.advanced=names.value:German%20Centre%20for%20Higher%20Education%20Research%20and%20Science%20Studies%20(DZHW)' | json_pp*/