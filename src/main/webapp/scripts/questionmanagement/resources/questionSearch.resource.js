'use strict';

angular.module('metadatamanagementApp').factory('QuestionSearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'questions';
  query.body = {};
  var findQuestions = function(questionIds) {
    query.body.query = {};
    query.body.query.docs = {
      'ids': questionIds
    };
    return ElasticSearchClient.mget(query);
  };
  var findQuestion = function(questionId, from, size) {
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
              'id': questionId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findPredeccessors = function(questionId, from, size) {
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
              'successors': questionId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findByProjectId = function(dataAcquisitionProjectId, from, size) {
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
    findQuestion: findQuestion,
    findByProjectId: findByProjectId
  };
});
