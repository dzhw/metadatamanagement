'use strict';

angular.module('metadatamanagementApp').factory('QuestionSearchService',
  function(ElasticSearchClient) {
    var query = {};
    query.type = 'questions';
    query.index = 'questions';
    var findQuestions = function(questionIds, selectedAttributes, from, size) {
      query.body = {};
      query.body.from = from;
      query.body.size = size;
      query.body._source = selectedAttributes;
      query.body.query = {};
      query.body.query.docs = {
        'ids': questionIds
      };
      return ElasticSearchClient.mget(query);
    };
    var findAllPredeccessors = function(questionId, selectedAttributes) {
      query.body = {};
      query.body._source = selectedAttributes;
      query.body.size = 1000;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'successors': questionId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    // jfdjfdjfdj
    var findByProjectId = function(dataAcquisitionProjectId, from, size) {
      query.body = {};
      query.body.from = from;
      query.body.size = size;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findByInstrumentId = function(instrumentId, from, size) {
      query.body = {};
      query.body.from = from;
      query.body.size = size;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'instrumentId': instrumentId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    //sdspüdpsdps
    var findByVariableId = function(variableId, selectedAttributes) {
      query.body = {};
      query.body._source = selectedAttributes;
      query.body.size = 1;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'variables.id': variableId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var countBy = function(term, value) {
      query.body = {};
      query.body.query = {};
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': []
        }
      };
      var subQuery = {
        'bool': {}
      };
      subQuery.bool.must = [];
      var mustSubQuery = {
        'term': {}
      };
      mustSubQuery.term[term] = value;
      subQuery.bool.must.push(mustSubQuery);
      query.body.query.bool.filter.push(subQuery);
      return ElasticSearchClient.count(query);
    };
    return {
      findAllPredeccessors: findAllPredeccessors,
      findSuccessors: findQuestions,
      findQuestions: findQuestions,
      findByInstrumentId: findByInstrumentId,
      findByProjectId: findByProjectId,
      findByStudyId: findByProjectId,
      findByVariableId: findByVariableId,
      countBy: countBy
    };
  });
