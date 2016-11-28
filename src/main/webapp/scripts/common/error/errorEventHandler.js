/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').run(
  function($rootScope) {
    $rootScope.$on('serverError', function(event, data) {
      console.log('Hello');
      console.log(event);
      console.log(data);
    });
  });
