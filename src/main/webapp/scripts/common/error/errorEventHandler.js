/* This Event Handler handles different Error Events bei the ErrorHandler
Interceptor. The Interceptor catches different Error of the 40X und 500 Errors.
The Interceptor sends an Event based on the catched Error. This Events will be
catched by this Event Handler. The Event Handler creates a Simple Message Toast
based on the Error Event.*/
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').run(
  function($rootScope, SimpleMessageToastService) {
    // Server or network down
    $rootScope.$on('serverNotReachableError', function() {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'server-not-reachable');
    });

    //Client Error 401
    $rootScope.$on('unauthorizedError', function(response) {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'client-error.unauthorized-error', {status: response.status});
    });

    //Client Error 403
    $rootScope.$on('forbiddenError', function(response) {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'client-error.forbidden-error', {status: response.status});
    });

    //Client Error 404
    $rootScope.$on('notFoundError', function(response) {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'client-error.not-found-error', {status: response.status});
    });

    //Server Error 500 to 511
    $rootScope.$on('internalServerError', function(response) {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'server-error.internal-server-error', {status: response.status});
    });
  });
