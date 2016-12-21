/* @Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').factory(
  'errorHandlerInterceptor',
  function($q, $rootScope, PageTitleService) {
    return {
      'responseError': function(response) {
        if (response.status === -1) {
          $rootScope.$emit('serverNotReachableError', response);
        }
        if (response.status === 401) {
          $rootScope.$emit('unauthorizedError', response);
        }
        if (response.status === 403) {
          $rootScope.$emit('forbiddenError', response);
        }
        if (response.status === 404) {
          $rootScope.$emit('notFoundError', response);
          PageTitleService.setPageTitle('global.title');
        }
        if (500 <= response.status && response.status <= 511) {
          $rootScope.$emit('internalServerError', response);
        }
        return $q.reject(response);
      }
    };
  });
