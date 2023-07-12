'use strict';

angular.module('metadatamanagementApp').factory(
  'RelatedPublicationRepositoryClient', function($http) {
    var findAll = function() {
      return $http({
        method: 'GET',
        url: '/api/related-publications/search/findAllBy',
        params: {
          projection: 'id-and-version'
        },
        transformResponse: function(data) {
          var response = angular.fromJson(data);
          if (response._embedded && response._embedded.relatedPublications) {
            return response._embedded.relatedPublications;
          }
          return response;
        }
      });
    };

    return {
      findAll: findAll
    };
  });
