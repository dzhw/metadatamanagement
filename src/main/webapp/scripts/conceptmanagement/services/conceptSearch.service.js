'use strict';

angular.module('metadatamanagementApp').factory('ConceptSearchService',
  function($q, ElasticSearchClient) {
    var createQueryObject = function(type) {
      type = type || 'concepts';
      return {
        index: type,
        type: type
      };
    };

    var findOneById = function(id, attributes) {
      var deferred = $q.defer();
      var query = createQueryObject();
      query.id = id;
      query._source = attributes;
      ElasticSearchClient.getSource(query, function(error, response) {
        if (error) {
          deferred.reject(error);
        } else {
          deferred.resolve(response);
        }
      });
      return deferred;
    };

    return {
      findOneById: findOneById
    };
  });
