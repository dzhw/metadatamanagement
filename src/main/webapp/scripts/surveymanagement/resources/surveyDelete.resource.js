'use strict';

angular.module('metadatamanagementApp')
  .factory('SurveyDeleteResource', function($resource) {
    return $resource(
      'api/surveys/delete', {}, {
        'deleteByDataAcquisitionProjectId': {
          method: 'POST',
          params: {
            dataAcquisitionProjectId: '@dataAcquisitionProjectId'
          }
        }
      });
  });
