'use strict';

angular.module('metadatamanagementApp')
  .factory('translationErrorHandler', function() {

    // has to return a function which gets a tranlation id
    return function(translationId) {
      return '{{' + translationId + '}}';
    };
  });
