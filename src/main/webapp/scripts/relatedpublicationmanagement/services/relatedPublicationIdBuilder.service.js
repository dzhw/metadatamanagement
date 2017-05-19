/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service(
  'RelatedPublicationIdBuilderService',
  function() {
    var buildRelatedPublicationId = function(id) {
      return 'pub-' + id + '$';
    };
    return {
      buildRelatedPublicationId: buildRelatedPublicationId
    };
  });
