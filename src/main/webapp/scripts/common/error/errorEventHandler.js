/* This Event Handler handles different Error Events bei the ErrorHandler
Interceptor. The Interceptor catches different Error of the 40X und 500 Errors.
The Interceptor sends an Event based on the catched Error. This Events will be
catched by this Event Handler. The Event Handler creates a Simple Message Toast
based on the Error Event.*/
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').run(
  function($rootScope, SimpleMessageToastService) {
    $rootScope.$on('serverError', function() {
      SimpleMessageToastService.openSimpleMessageToast('global.error.' +
        'server-error.internal-server-error');
    });
  });
