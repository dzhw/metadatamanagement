'use strict';

angular.module('metadatamanagementApp').factory('SurveySearchService',
function(Language, ElasticSearchClient) {
  var query = {};
  query.type = 'surveys';
  query.body = {};
  var findSurveys = function(surveyIds) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
    query.body.query = {};
    query.body.query.docs = {
      'ids': surveyIds
    };
    return ElasticSearchClient.mget(query);
  };
  var findByProjectId = function(dataAcquisitionProjectId, from, size,
    excludSurvey) {
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
        'must_not': {
          'term': {
            'id': excludSurvey || ''
          }
        },
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
  var countBy = function(term, value) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
    query.body.query = {
      'term': {}
    };
    query.body.query.term[term] = value;
    return ElasticSearchClient.count(query);
  };
  return {
    findSurveys: findSurveys,
    findByProjectId: findByProjectId,
    countBy: countBy
  };
});
