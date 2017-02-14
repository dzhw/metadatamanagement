'use strict';

angular.module('metadatamanagementApp').factory('InstrumentSearchService',
  function(ElasticSearchClient) {
    var query = {};
    query.type = 'instruments';
    query.index = 'instruments';

    var findInstruments = function(instruments) {
      query.body = {};
      query.body.query = {};
      query.body.query.docs = {
        'ids': instruments
      };
      return ElasticSearchClient.mget(query);
    };

    var findBySurveyId = function(surveyId, from, size) {
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
              'surveyIds': surveyId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findByProjectId = function(dataAcquisitionProjectId, from, size) {
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
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var countBy = function(term, value) {
      query.body = {};
      query.body.query = {
        'term': {}
      };
      query.body.query.term[term] = value;
      return ElasticSearchClient.count(query);
    };
    return {
      findInstruments: findInstruments,
      findBySurveyId: findBySurveyId,
      findByProjectId: findByProjectId,
      countBy: countBy
    };
  });
