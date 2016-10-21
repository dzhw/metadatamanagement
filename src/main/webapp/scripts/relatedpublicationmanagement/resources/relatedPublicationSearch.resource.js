'use strict';

angular.module('metadatamanagementApp')
.factory('RelatedPublicationSearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'related_publications';
  query.body = {};

  var findBySurveyId = function(surveyId) {
    query.filterPath = '';
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
  var findByQuestionId = function(questionId) {
    query.filterPath = '';
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
  var findByVariableId = function(variableId) {
    query.filterPath = '';
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
  var findByDataSetId = function(dataSetId) {
    query.filterPath = '';
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
  var findByStudyId = function(studyId) {
    query.filterPath = '';
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
  return {
    findBySurveyId: findBySurveyId,
    findByVariableId: findByVariableId,
    findByDataSetId: findByDataSetId,
    findByQuestionId: findByQuestionId,
    findByStudyId: findByStudyId
  };
});
