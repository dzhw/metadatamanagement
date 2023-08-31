'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllScriptAttachmentsResource', ['$resource',  function($resource) {
    return $resource(
      '/api/analysis-packages/:analysisPackageId' +
      '/scripts/attachments/', {
        analysisPackageId: '@id'
      }, {
        'deleteAll': {
          method: 'DELETE'
        }
      });
  }]);

