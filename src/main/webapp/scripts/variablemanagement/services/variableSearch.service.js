'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchService',
  function(ElasticSearchClient) {
    var query = {};
    query.type = 'variables';
    query.index = 'variables';

    var findVariables = function(variableIds) {
      query.body = {};
      query.body.query = {};
      query.body.query.docs = {
        'ids': variableIds
      };
      return ElasticSearchClient.mget(query);
    };
    var findBySurveyTitle = function(surveyTitle, from, size) {
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
              'surveyTitles': surveyTitle
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findByQuestionId = function(questionId, from, size) {
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
              'questionId': questionId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findByDataSetId = function(dataSetId, from, size) {
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
              'dataSetId': dataSetId
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
      findByQuestionId: findByQuestionId,
      findBySurveyTitle: findBySurveyTitle,
      findVariables: findVariables,
      findByDataSetId: findByDataSetId,
      countBy: countBy
    };
  });
