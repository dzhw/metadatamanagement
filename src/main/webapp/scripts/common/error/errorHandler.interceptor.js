/* @Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').factory(
  'errorHandlerInterceptor',
  function($q, $rootScope) {
    return {
      'responseError': function(response) {
        if (response.status === 401) {
          $rootScope.$emit('unauthorizedError', response);
        }
        if (response.status === 403) {
          $rootScope.$emit('forbiddenError', response);
        }
        if (response.status === 404) {
          $rootScope.$emit('notFoundError', response);
        }
        if (500 <= response.status && response.status <= 511) {
          $rootScope.$emit('internalServerError', response);
        }
        return $q.reject(response);
      }
    };
  });
