'use strict';

angular.module('metadatamanagementApp').factory('Password',
    function($resource) {
      return $resource('api/account/change-password', {}, {});
    });

angular.module('metadatamanagementApp').factory('PasswordResetInit',
    function($resource) {
      return $resource('api/account/reset-password/init', {}, {});
    });

angular.module('metadatamanagementApp').factory('PasswordResetFinish',
    function($resource) {
      return $resource('api/account/reset-password/finish', {}, {});
    });
