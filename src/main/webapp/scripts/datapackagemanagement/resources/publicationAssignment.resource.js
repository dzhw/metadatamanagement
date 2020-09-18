/* Author: Daniel Katzberg */
'use strict';

/* Data Package Publication Assignment Resource */
angular.module('metadatamanagementApp')
  .factory('PublicationAssignmentResource', function($resource) {
    return $resource(
      '/api/data-packages/:dataPackageId/publications/:publicationId', {
      dataPackageId: '@dataPackageId',
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
