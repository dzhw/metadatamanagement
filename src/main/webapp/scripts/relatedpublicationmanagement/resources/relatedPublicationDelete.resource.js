'use strict';
/* @author Daniel Katzberg */

angular.module('metadatamanagementApp')
  .factory('RelatedPublicationDeleteResource', function($resource) {
    return $resource(
      'api/related-publications/delete-all', {}, {
        'deleteAll': {
          method: 'POST'
        }
      });
  });
