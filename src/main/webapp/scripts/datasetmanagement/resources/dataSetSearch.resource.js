'use strict';

angular.module('metadatamanagementApp').factory('DataSetSearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'data_sets';
  query.body = {};

  var findDataSets = function(dataSetIds) {
    query.body.query = {};
    query.body.query.docs = {
      'ids': dataSetIds
    };
    return ElasticSearchClient.mget(query);
  };
  var findByVariableId = function(variableId) {
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
  var findBySurveyTitle = function(surveyTitle) {
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
  var findByProjectId = function(dataAcquisitionProjectId) {
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
    findByVariableId: findByVariableId,
    findBySurveyTitle: findBySurveyTitle,
    findByProjectId: findByProjectId,
    findDataSets: findDataSets
  };
});
