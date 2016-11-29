/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').run(
  function($rootScope, SimpleMessageToastService) {
    $rootScope.$on('serverError', function(event, data) {
      console.log(event);
      console.log(data);
      SimpleMessageToastService.openSimpleMessageToast('lalalalala',
        null);
    });
  });
