'use strict';

angular.module('metadatamanagementApp')
  .factory('RelatedPublicationSearchService',
    function(ElasticSearchClient, $q) {
      var createQueryObject = function() {
        return {
          index: 'related_publications',
          type: 'related_publications'
        };
      };

      var findOneById = function(id) {
        var deferred = $q.defer();
        var query = createQueryObject();
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
      var findBySurveyId = function(surveyId, selectedAttributes, from,
        size) {
        var query = createQueryObject();
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
        var query = createQueryObject();
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
        var query = createQueryObject();
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
        var query = createQueryObject();
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
        var query = createQueryObject();
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
      var findByStudyId = function(studyId, selectedAttributes, from,
        size) {
        var query = createQueryObject();
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
        var query = createQueryObject();
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
        findBySurveyId: findBySurveyId,
        findByVariableId: findByVariableId,
        findByDataSetId: findByDataSetId,
        findByQuestionId: findByQuestionId,
        findByStudyId: findByStudyId,
        findByInstrumentId: findByInstrumentId,
        countBy: countBy
      };
    });
