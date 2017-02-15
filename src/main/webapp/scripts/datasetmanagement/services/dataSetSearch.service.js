/* global  _*/
'use strict';

angular.module('metadatamanagementApp').factory('DataSetSearchService',
  function(ElasticSearchClient) {
    var query = {};
    query.type = 'data_sets';
    query.index = 'data_sets';

    var findDataSets = function(dataSetIds, selectedAttributes) {
      var ids = _.split(dataSetIds, ',');
      query.body = {};
      query.body.query = {};
      query.body.query.docs = [];
      _.forEach(ids, function(id) {
        query.body.query.docs.push({
          '_id': id,
          '_source': {
              'include': selectedAttributes
            }
        });
      });
      return ElasticSearchClient.mget(query);
    };
    var findOneByVariableId = function(variableId, selectedAttributes) {
      query.body = {};
      query.body.size = 1;
      query.body._source = selectedAttributes;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'variables.id': variableId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findBySurveyId = function(surveyId, selectedAttributes, from,
      size) {
      query.body = {};
      query.body.from = from;
      query.body.size = size;
      query.body._source = selectedAttributes;
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
    var findByProjectId = function(dataAcquisitionProjectId, selectedAttributes,
      from, size,
      excludedDataSetId) {
      query.body = {};
      query.body.from = from;
      query.body.size = size;
      query.body._source = selectedAttributes;
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
      findOneByVariableId: findOneByVariableId,
      findBySurveyId: findBySurveyId,
      findByProjectId: findByProjectId,
      findDataSets: findDataSets,
      countBy: countBy
    };
  });
