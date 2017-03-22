/* global _*/
'use strict';

angular.module('metadatamanagementApp').factory('InstrumentSearchService',
  function(ElasticSearchClient, $q) {
    var query = {};
    query.type = 'instruments';
    query.index = 'instruments';

    var findOneById = function(id) {
      var deferred = $q.defer();
      delete query.body;
      query.id = id;
      ElasticSearchClient.getSource(query, function(error, response) {
          if (error) {
            deferred.reject(error);
          } else {
            deferred.resolve(response);
          }
        });
      return deferred;
    };

    var findInstruments = function(instrumentIds, selectedAttributes) {
      var ids = _.split(instrumentIds, ',');
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
      from, size) {
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
      return ElasticSearchClient.search(query);
    };
    var countBy = function(term, value) {
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
      var mustTerm = {
        'term': {}
      };
      mustTerm.term[term] = value;
      query.body.query.bool.filter.push(mustTerm);
      return ElasticSearchClient.count(query);
    };
    return {
      findOneById: findOneById,
      findInstruments: findInstruments,
      findBySurveyId: findBySurveyId,
      findByProjectId: findByProjectId,
      countBy: countBy
    };
  });
