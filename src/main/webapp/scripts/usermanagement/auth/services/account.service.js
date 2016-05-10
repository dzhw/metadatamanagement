'use strict';

angular.module('metadatamanagementApp').factory('AccountResource',
    function AccountResource($resource) {
      return $resource('api/account', {}, {
        'get': {
          method: 'GET',
          params: {},
          isArray: false,
          interceptor: {
            response: function(response) {
              // expose response
              return response;
            }
          }
        }
      });
    });
