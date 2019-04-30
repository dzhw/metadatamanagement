/* Author: Daniel Katzberg */
'use strict';

/* Study Resource */
angular.module('metadatamanagementApp')
  .factory('PublicationAssignmentResource', function($resource) {
    return $resource('/api/studies/:studyId/publications/:publicationId', {
      studyId: '@studyId',
      publicationId: '@publicationId',
    }, {
      'save': {
        method: 'PUT',
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });
