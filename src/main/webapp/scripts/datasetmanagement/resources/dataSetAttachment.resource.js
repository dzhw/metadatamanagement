'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('DataSetAttachmentResource', function($resource) {
    return $resource('/api/data-sets/:id/attachments', {
      id: '@id'
    }, {
      'findByDataSetId': {
        method: 'GET',
        isArray: true
      }
    });
  });
