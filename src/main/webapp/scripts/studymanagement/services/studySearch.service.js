'use strict';

angular.module('metadatamanagementApp').factory('StudySearchService',
  function(ElasticSearchClient) {
    var query = {};
    query.type = 'studies';
    query.index = 'studies';
    query.body = {};

    var findStudies = function(studyIds) {
      query.body.query = {};
      query.body.query.docs = {
        'ids': studyIds
      };
      return ElasticSearchClient.mget(query);
    };
    var findStudy = function(studyId, from, size) {
      query.body.from = from;
      query.body.size = size;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'id': studyId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    return {
      findStudies: findStudies,
      findStudy: findStudy
    };
  });
