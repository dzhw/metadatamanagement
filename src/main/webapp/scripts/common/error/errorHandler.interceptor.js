/* @Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').factory(
  'errorHandlerInterceptor',
  function($q, $rootScope) {
    return {
      'responseError': function(response) {
        if (response.status === 404) {
          $rootScope.$emit('notFoundError', response);
        }
        if (response.status === 500) {
          $rootScope.$emit('serverError', response);
        }
        return $q.reject(response);
      }
    };
  });
