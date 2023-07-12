'use strict';

angular.module('metadatamanagementApp').factory('PasswordResource',
    function($resource) {
      return $resource('api/account/change-password', {}, {});
    });

angular.module('metadatamanagementApp').factory('PasswordResetInitResource',
    function($resource) {
      return $resource('api/account/reset-password/init', {}, {});
    });

angular.module('metadatamanagementApp').factory('PasswordResetFinishResource',
    function($resource) {
      return $resource('api/account/reset-password/finish', {}, {});
    });
