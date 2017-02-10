'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchService',
  function(LanguageService, ElasticSearchClient) {
    var query = {};
    query.type = 'variables';

    var findVariables = function(variableIds) {
      query.index = 'metadata_' + LanguageService.getCurrentInstantly();
      query.body = {};
      query.body.query = {};
      query.body.query.docs = {
        'ids': variableIds
      };
      return ElasticSearchClient.mget(query);
    };
    var findBySurveyTitle = function(surveyTitle, from, size) {
      query.index = 'metadata_' + LanguageService.getCurrentInstantly();
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
      query.index = 'metadata_' + LanguageService.getCurrentInstantly();
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
      query.index = 'metadata_' + LanguageService.getCurrentInstantly();
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
      query.index = 'metadata_' + LanguageService.getCurrentInstantly();
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
