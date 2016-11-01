'use strict';

angular.module('metadatamanagementApp')
  .factory('InstrumentDeleteResource', function($resource) {
    return $resource(
      'api/instruments/delete', {}, {
        'deleteByDataAcquisitionProjectId': {
          method: 'POST',
          params: {
            dataAcquisitionProjectId: '@dataAcquisitionProjectId'
          }
        }
      });
  });
