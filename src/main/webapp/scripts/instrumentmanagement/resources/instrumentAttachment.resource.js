'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('InstrumentAttachmentResource', function($resource) {
    return $resource('/api/instruments/:id/attachments', {
      id: '@id'
    }, {
      'findByInstrumentId': {
        method: 'GET',
        isArray: true
      }
    });
  });
