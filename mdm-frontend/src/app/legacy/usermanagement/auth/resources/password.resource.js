'use strict';

angular.module('metadatamanagementApp').factory('PasswordResource', ['$resource',
    function($resource) {
      return $resource('api/account/change-password', {}, {});
    }]);

angular.module('metadatamanagementApp').factory('PasswordResetInitResource', ['$resource',
    function($resource) {
      return $resource('api/account/reset-password/init', {}, {});
    }]);

angular.module('metadatamanagementApp').factory('PasswordResetFinishResource', ['$resource',
    function($resource) {
      return $resource('api/account/reset-password/finish', {}, {});
    }]);
