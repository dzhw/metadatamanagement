'use strict';

angular.module('metadatamanagementApp').factory('QuestionSearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'questions';
  query.body = {};
  var findQuestions = function(questionIds) {
    query.filterPath = 'docs._source';
    query.body.query = {};
    query.body.query.docs = {
      'ids': questionIds
    };
    return ElasticSearchClient.mget(query);
  };
  var findPredeccessors = function(questionId) {
    query.filterPath = 'hits.hits._source';
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
              'successors': questionId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findByProjectId = function(dataAcquisitionProjectId) {
    query.filterPath = 'hits.hits._source';
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
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  return {
    findPredeccessors: findPredeccessors,
    findSuccessors: findQuestions,
    findQuestions: findQuestions,
    findByProjectId: findByProjectId
  };
});
