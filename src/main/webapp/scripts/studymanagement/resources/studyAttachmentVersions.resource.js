'use strict';

/* Study Versions Resource */
angular.module('metadatamanagementApp')
  .factory('StudyAttachmentVersionsResource', function($resource) {
    return $resource('/api/studies/:studyId/attachments/:filename/versions', {
      studyId: '@studyId',
      filename: '@filename'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });
