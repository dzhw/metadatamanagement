/* @Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').factory(
  'errorHandlerInterceptor',
  function($q, $rootScope) {
    return {
      'responseError': function(response) {
        if (response.status === 404) {
          console.log('notfound');
          $rootScope.$emit('notFoundError', response);
        }
        if (response.status === 500) {
          console.log('serverError');
          $rootScope.$emit('serverError', response);
        }
        return $q.reject(response);
      }
    };
  });
