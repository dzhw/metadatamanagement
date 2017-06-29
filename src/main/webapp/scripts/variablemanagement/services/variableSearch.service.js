/* global  _*/
'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchService',
  function(ElasticSearchClient, $q) {
    var createQueryObject = function() {
      return {
        index: 'variables',
        type: 'variables'
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

    var findVariables = function(variableIds, selectedAttributes) {
      var ids = _.split(variableIds, ',');
      var query = createQueryObject();
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
    var findBySurveyTitle = function(surveyTitle, selectedAttributes, from,
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
              'surveyTitles': surveyTitle
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
              'relatedQuestions.questionId': questionId
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
              'dataSetId': dataSetId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findByDataSetIdAndIndexInDataSet = function(dataSetId, indexInDataSet,
      selectedAttributes, from, size) {
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
              'dataSetId': dataSetId
            }
          },{
            'term': {
              'indexInDataSet': indexInDataSet
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var countBy = function(term, value, dataSetId) {
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
      if (dataSetId) {
        query.body.query.bool.filter.push({
          'term': {
            'dataSetId': dataSetId
          }});
      }
      return ElasticSearchClient.count(query);
    };
    return {
      findOneById: findOneById,
      findByQuestionId: findByQuestionId,
      findBySurveyTitle: findBySurveyTitle,
      findVariables: findVariables,
      findByDataSetId: findByDataSetId,
      findByDataSetIdAndIndexInDataSet: findByDataSetIdAndIndexInDataSet,
      countBy: countBy
    };
  });
