'use strict';

angular.module('metadatamanagementApp').factory('DataSetSearchService',
  function(LanguageService, ElasticSearchClient) {
    var query = {};
    query.type = 'data_sets';

    var findDataSets = function(dataSetIds) {
      query.index = 'metadata_' + LanguageService.getCurrentInstantly();
      query.body = {};
      query.body.query = {};
      query.body.query.docs = {
        'ids': dataSetIds
      };
      return ElasticSearchClient.mget(query);
    };
    var findByVariableId = function(variableId, from, size) {
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
              'variableIds': variableId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findBySurveyId = function(surveyId, from, size) {
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
              'surveyIds': surveyId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findByProjectId = function(dataAcquisitionProjectId, from, size,
      excludedDataSetId) {
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
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          }]
        }
      };
      if (excludedDataSetId) {
        // jscs:disable
        query.body.query.bool.must_not = {
          'term': {
            'id': excludedDataSetId
          }
        };
        // jscs:enable
      }
      return ElasticSearchClient.search(query);
    };
    var countBy = function(term, value, excludedDataSetId) {
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
      if (excludedDataSetId) {
        // jscs:disable
        subQuery.bool.must_not = [{
          'term': {
            'id': excludedDataSetId
          }
        }];
        // jscs:enable
      }
      query.body.query.bool.filter.push(subQuery);
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
