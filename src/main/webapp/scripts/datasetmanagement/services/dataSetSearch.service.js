'use strict';

angular.module('metadatamanagementApp').factory('DataSetSearchService',
function(Language, ElasticSearchClient) {
  var query = {};
  query.type = 'data_sets';

  var findDataSets = function(dataSetIds) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
    query.body = {};
    query.body.query = {};
    query.body.query.docs = {
      'ids': dataSetIds
    };
    return ElasticSearchClient.mget(query);
  };
  var findByVariableId = function(variableId, from, size) {
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
              'variableIds': variableId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  var findBySurveyId = function(surveyId, from, size) {
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
              'surveyIds': surveyId
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
  var countBy  = function(term, value) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
    query.body = {};
    query.body.query = {
      'term': {}
    };
    query.body.query.term[term] = value;
    return ElasticSearchClient.count(query);
  };
  return {
    findByVariableId: findByVariableId,
    findBySurveyId: findBySurveyId,
    findByProjectId: findByProjectId,
    findDataSets: findDataSets,
    countBy: countBy
  };
});
