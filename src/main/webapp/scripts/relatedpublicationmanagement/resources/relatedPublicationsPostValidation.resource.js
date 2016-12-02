'use strict';
/*
  Resource url definition for the post validation..
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp')
  .factory('RelatedPublicationsPostValidationResource', function($resource) {
    return $resource('/api/related-publications/post-validate', {}, {
      'postValidate': {
        method: 'POST'
      }
    });
  });
