'use strict';

angular.module('metadatamanagementApp').factory(
  'errorHandlerInterceptor',
  function($q, $rootScope) {
    return {
      'responseError': function(response) {
        if (!(response.status === 401 && response.data.path
            .indexOf('/api/account') === 0)) {
          $rootScope.$emit('httpError', response);
        }
        if (response.status === 500) {
          console.log('server Fehler');
          $rootScope.$emit('serverError', 'some data');
        }
        return $q.reject(response);
      }
    };
  });
