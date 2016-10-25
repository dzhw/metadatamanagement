'use strict';

angular.module('metadatamanagementApp').factory('SurveySearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'surveys';
  query.body = {};
  var findSurveys = function(surveyIds) {
    query.body.query = {};
    query.body.query.docs = {
      'ids': surveyIds
    };
    return ElasticSearchClient.mget(query);
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
    findSurveys: findSurveys,
    findByProjectId: findByProjectId
  };
});
