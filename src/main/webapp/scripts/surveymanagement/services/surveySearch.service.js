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
        'filter': [
          {
            'term': {
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          }
        ]
      }
    };
    if (excludSurvey) {
      // jscs:disable
      query.body.query.bool.must_not = {
        'term': {
          'id': excludSurvey
        }
      };
      // jscs:enable
    }
    return ElasticSearchClient.search(query);
  };
  var countBy = function(term, value) {
    query.index = 'metadata_' + Language.getCurrentInstantly();
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
    var subQuery = {'term': {}};
    subQuery.term[term] = value;
    query.body.query.bool.filter.push(subQuery);
    return ElasticSearchClient.count(query);
  };
  return {
    findSurveys: findSurveys,
    findByProjectId: findByProjectId,
    countBy: countBy
  };
});
