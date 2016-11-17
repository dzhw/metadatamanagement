'use strict';

angular.module('metadatamanagementApp').factory('QuestionSearchService',
function(Language, ElasticSearchClient) {
  var query = {};
  query.type = 'questions';
  var findQuestions = function(questionIds) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
    query.body = {};
    query.body.query = {};
    query.body.query.docs = {
      'ids': questionIds
    };
    return ElasticSearchClient.mget(query);
  };
  var findPredeccessors = function(questionId, from, size) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
    query.body = {};
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
    query.index = 'metadata_' + Language.getCurrentInstantly();
    query.body = {};
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
  var findByInstrumentId = function(instrumentId, from, size) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
    query.body = {};
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
              'instrumentId': instrumentId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var countBy = function(term, value) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
    query.body = {};
    query.body.query = {
      'term': {}
    };
    query.body.query.term[term] = value;
    return ElasticSearchClient.count(query);
  };
  return {
    findPredeccessors: findPredeccessors,
    findSuccessors: findQuestions,
    findQuestions: findQuestions,
    findByInstrumentId: findByInstrumentId,
    findByProjectId: findByProjectId,
    findByStudyId: findByProjectId,
    countBy: countBy
  };
});
