/* global _*/
'use strict';

angular.module('metadatamanagementApp').factory('QuestionSearchService',
  function(ElasticSearchClient, $q) {
    var createQueryObject = function() {
      return {
        index: 'questions',
        type: 'questions'
      };
    };

    var findOneById = function(id) {
      var deferred = $q.defer();
      var query =  createQueryObject();
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

    var findAllSuccessors = function(questionIds, selectedAttributes, from,
      size) {
      var ids = _.split(questionIds, ',');
      var query =  createQueryObject();
      query.body = {};
      query.body.from = from;
      query.body.size = size;
      query.body.query = {};
      query.body.query.ids = {values: ids};
      query.body._source = selectedAttributes;
      query.body.sort = [
        {
          'indexInInstrument': {
            'order': 'asc'
          }
        }
      ];
      return ElasticSearchClient.search(query);
    };

    var findAllPredeccessors = function(questionId, selectedAttributes, from,
      size) {
      var query =  createQueryObject();
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
              'successors': questionId
            }
          }]
        }
      };
      query.body.sort = [
        {
          'indexInInstrument': {
            'order': 'asc'
          }
        }
      ];
      return ElasticSearchClient.search(query);
    };
    var findByProjectId = function(dataAcquisitionProjectId, selectedAttributes,
      from, size) {
      var query =  createQueryObject();
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
    var findByVariableId = function(variableId, selectedAttributes, from,
      size) {
      var query =  createQueryObject();
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
    var countBy = function(term, value) {
      var query =  createQueryObject();
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
      findAllPredeccessors: findAllPredeccessors,
      findAllSuccessors: findAllSuccessors,
      findByProjectId: findByProjectId,
      findByStudyId: findByProjectId,
      findByVariableId: findByVariableId,
      countBy: countBy
    };
  });
