'use strict';

angular.module('metadatamanagementApp')
.factory('RelatedPublicationSearchService',
function(Language, ElasticSearchClient) {
  var query = {};
  query.type = 'related_publications';
  query.body = {};

  var findBySurveyId = function(surveyId, from, size) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
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
              'surveyIds': surveyId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findByQuestionId = function(questionId, from, size) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
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
              'questionIds': questionId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findByVariableId = function(variableId, from, size) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
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
              'variableIds': variableId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findByDataSetId = function(dataSetId, from, size) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
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
              'dataSetIds': dataSetId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findByProjectId = function(studyId, from, size) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
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
              'studyIds': studyId
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
    query.body.query = {};
    query.body.query = {
      'bool': {
        'must': [
          {
            'match_all': {}
          }
        ],
        'filter': []
      }
    };
    var subQuery = {'bool': {}};
    subQuery.bool.must = [];
    var mustSubQuery = {'term': {}};
    mustSubQuery.term[term] = value;
    subQuery.bool.must.push(mustSubQuery);
    query.body.query.bool.filter.push(subQuery);
    return ElasticSearchClient.count(query);
  };
  return {
    findBySurveyId: findBySurveyId,
    findByVariableId: findByVariableId,
    findByDataSetId: findByDataSetId,
    findByQuestionId: findByQuestionId,
    findByProjectId: findByProjectId,
    findByStudyId: findByProjectId,
    countBy: countBy
  };
});
