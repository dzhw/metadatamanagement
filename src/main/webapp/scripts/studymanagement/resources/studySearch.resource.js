'use strict';

angular.module('metadatamanagementApp').factory('StudySearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'studies';
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
        'must': [
          {
            'match_all': {}
          }
        ],
        'filter': [
          {
            'term': {
              'id': studyId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  return {
    findStudies: findStudies,
    findStudy: findStudy
  };
});
