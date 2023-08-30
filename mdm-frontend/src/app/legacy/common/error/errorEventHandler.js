/* This Event Handler handles different Error Events bei the ErrorHandler
Interceptor. The Interceptor catches different Error of the 40X und 500 Errors.
The Interceptor sends an Event based on the catched Error. This Events will be
catched by this Event Handler. The Event Handler creates a Simple Message Toast
based on the Error Event.*/
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').run([
  '$rootScope',
  '$state',
  'SimpleMessageToastService',
  'PageMetadataService',
  'LanguageService',
  'Auth',
  'Principal',
  function($rootScope, $state, SimpleMessageToastService, PageMetadataService,
           LanguageService, Auth, Principal) {
    var ignore404 = 0;
    var ignore401 = 0;
    var ignore504 = 0;

    $rootScope.$on('start-ignoring-404', function() {
      ignore404++;
    });

    $rootScope.$on('stop-ignoring-404', function() {
      ignore404 = Math.max(ignore404 - 1, 0);
    });

    $rootScope.$on('start-ignoring-401', function() {
      ignore401++;
    });

    $rootScope.$on('stop-ignoring-401', function() {
      ignore401 = Math.max(ignore401 - 1, 0);
    });

    $rootScope.$on('start-ignoring-504', function() {
      ignore504++;
    });

    $rootScope.$on('stop-ignoring-504', function() {
      ignore504 = Math.max(ignore504 - 1, 0);
    });

    // Server or network down
    $rootScope.$on('serverNotReachableError', function() {
      SimpleMessageToastService.openAlertMessageToast('global.error.' +
        'server-not-reachable');
    });

    //Client Error 401
    $rootScope.$on('unauthorizedError',
    function(event, response) { // jshint ignore:line
      if (ignore401 === 0) {
        SimpleMessageToastService.openAlertMessageToast('global.error.' +
          'client-error.unauthorized-error', {status: response.status});
        $rootScope.$broadcast('userNotAuthorized');
      }
    });

    //Client Error 403
    $rootScope.$on('forbiddenError',
    function(event, response) { // jshint ignore:line
      SimpleMessageToastService.openAlertMessageToast('global.error.' +
        'client-error.forbidden-error', {status: response.status});
    });

    //Client Error 404
    $rootScope.$on('notFoundError',
    function(event, response) { // jshint ignore:line
      if (ignore404 === 0) {
        SimpleMessageToastService.openAlertMessageToast('global.error.' +
          'client-error.not-found-error', {status: response.status});
        PageMetadataService.setPageTitle('global.title');
      }
    });

    //Server Error 500 to 511
    $rootScope.$on('internalServerError',
    function(event, response) { // jshint ignore:line
      SimpleMessageToastService.openAlertMessageToast('global.error.' +
        'server-error.internal-server-error', {status: response.status});
    });

    //Server Error 504
    $rootScope.$on('gatewayTimeout',
    function(event, response) { // jshint ignore:line
      if (ignore504 === 0) {
        SimpleMessageToastService.openAlertMessageToast('global.error.' +
          'server-error.gateway-timeout', {status: response.status});
      }
    });

    // user not authorized broadcast
    var inTransition = false;
    $rootScope.$on('userNotAuthorized',
    function(event, response) { // jshint ignore:line
      // user is not authenticated. store the state
      // they wanted before you
      // send them to the signin state, so you can
      // return them when you're done
      if (!Principal.isAuthenticated() && !inTransition &&
          $rootScope.toStateName &&
          $rootScope.toStateName !== 'login' &&
          $rootScope.toStateName !== 'register') {
        inTransition = true;
        Auth.logout();
        $rootScope.previousStateName = $rootScope.toStateName;
        $rootScope.previousStateParams = $rootScope.toStateParams;
        $state.go('login', {
          lang: LanguageService.getCurrentInstantly()
        }).then(function() {
          inTransition = false;
        });
      }
    });
  }]);
