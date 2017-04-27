'use strict';

/* Study Attachment Resource */
angular.module('metadatamanagementApp')
.factory('StudyAttachmentResource', function($resource) {
      return $resource('/api/studies/:id/attachments', {
        id: '@id'
      }, {
        'findByStudyId': {
          method: 'GET',
          isArray: true
        }
      });
    });
