/* This Event Handler handles different Error Events bei the ErrorHandler
Interceptor. The Interceptor catches different Error of the 40X und 500 Errors.
The Interceptor sends an Event based on the catched Error. This Events will be
catched by this Event Handler. The Event Handler creates a Simple Message Toast
based on the Error Event.*/
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').run(
  function($rootScope, SimpleMessageToastService) {

    //Client Error 401
    $rootScope.$on('unauthorizedError', function() {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'client-error.unauthorized-error');
    });

    //Client Error 403
    $rootScope.$on('forbiddenError', function() {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'client-error.forbidden-error');
    });

    //Client Error 404
    $rootScope.$on('notFoundError', function() {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'client-error.not-found-error');
    });

    //Server Error 500 to 511
    $rootScope.$on('internalServerError', function() {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'server-error.internal-server-error');
    });
  });
