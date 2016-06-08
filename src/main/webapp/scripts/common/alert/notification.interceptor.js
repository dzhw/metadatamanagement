'use strict';

angular.module('metadatamanagementApp').factory('notificationInterceptor',
  function($q, AlertService) {
    return {
      response: function(response) {
        var alertKey = response.headers('X-metadatamanagementApp-alert');
        if (angular.isString(alertKey)) {
          AlertService.success(alertKey, {
            param: response.headers('X-metadatamanagementApp-params')
          });
        }
        return response;
      }
    };
  });
