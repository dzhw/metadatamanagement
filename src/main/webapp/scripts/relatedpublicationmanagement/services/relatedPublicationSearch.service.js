'use strict';

angular.module('metadatamanagementApp')
  .factory('RelatedPublicationSearchService',
    function(ElasticSearchClient) {
      var query = {};
      query.type = 'related_publications';
      query.index = 'related_publications';

      var findBySurveyId = function(surveyId, selectedAttributes, from,
        size) {
        query.body = {};
        query.body.from = from;
        query.body.size = size;
        query.body._source = selectedAttributes;
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
      var findByQuestionId = function(questionId, selectedAttributes, from,
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
                'questionIds': questionId
              }
            }]
          }
        };
        return ElasticSearchClient.search(query);
      };
      var findByVariableId = function(variableId, selectedAttributes, from,
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
                'variables.id': variableId
              }
            }]
          }
        };
        return ElasticSearchClient.search(query);
      };
      var findByInstrumentId = function(instrumentId, selectedAttributes,
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
                'instrumentIds': instrumentId
              }
            }]
          }
        };
        return ElasticSearchClient.search(query);
      };
      var findByDataSetId = function(dataSetId, selectedAttributes, from,
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
                'dataSetIds': dataSetId
              }
            }]
          }
        };
        return ElasticSearchClient.search(query);
      };
      var findByProjectId = function(studyId, selectedAttributes, from,
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
                'studyIds': studyId
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
        var subQuery = {
          'bool': {}
        };
        subQuery.bool.must = [];
        var mustSubQuery = {
          'term': {}
        };
        mustSubQuery.term[term] = value;
        subQuery.bool.must.push(mustSubQuery);
        query.body.query.bool.filter.push(subQuery);
        return ElasticSearchClient.count(query);
      };
      return {
        findBySurveyId: findBySurveyId,
        findByVariableId: findByVariableId,
        findByDataSetId: findByDataSetId,
        findByQuestionId: findByQuestionId,
        findByProjectId: findByProjectId,
        findByStudyId: findByProjectId,
        findByInstrumentId: findByInstrumentId,
        countBy: countBy
      };
    });
