'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'variables';
  query.body = {};

  var findVariables = function(variableIds) {
    query.body.query = {};
    query.body.query.docs = {
      'ids': variableIds
    };
    return ElasticSearchClient.mget(query);
  };
  var findBySurveyTitle = function(surveyTitle, from, size) {
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
              'surveyTitles': surveyTitle
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findByQuestionId = function(questionId, from, size) {
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
              'questionId': questionId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var getCounts = function(term, value) {
    query.body.query = {
      'term': {}
    };
    query.body.query.term[term] = value;
    return ElasticSearchClient.count(query);
  };
  return {
    findByQuestionId: findByQuestionId,
    findBySurveyTitle: findBySurveyTitle,
    findVariables: findVariables,
    getCounts: getCounts
  };
});
