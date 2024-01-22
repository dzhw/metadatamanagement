'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectTweetResource', ['$resource',  function($resource) {
    return $resource('/api/data-acquisition-projects/tweet', {}, {
      'createTweet': {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        transformResponse: function (data) {
          return { response: data };
        }
      }
    });
  }]);

