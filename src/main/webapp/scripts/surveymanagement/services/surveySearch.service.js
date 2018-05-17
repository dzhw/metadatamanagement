/* global  _*/
'use strict';

angular.module('metadatamanagementApp').factory('SurveySearchService',
  function(ElasticSearchClient, $q, LanguageService, SearchHelperService,
    CleanJSObjectService) {
    var createQueryObject = function() {
      return {
        index: 'surveys',
        type: 'surveys'
      };
    };

    var createTermFilters = function(filter, dataAcquisitionProjectId) {
      var termFilter;
      if (!CleanJSObjectService.isNullOrEmpty(filter) ||
        !CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        termFilter = [];
      }
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        var projectFilter = {
          term: {
            dataAcquisitionProjectId: dataAcquisitionProjectId
          }
        };
        termFilter.push(projectFilter);
      }
      if (!CleanJSObjectService.isNullOrEmpty(filter)) {
        termFilter = _.concat(termFilter,
          SearchHelperService.createTermFilters('surveys', filter));
      }
      return termFilter;
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
    var findSurveys = function(surveyIds, selectedAttributes) {
      var ids = _.split(surveyIds, ',');
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
    var findByProjectId = function(dataAcquisitionProjectId, selectedAttributes,
      from, size, excludedSurveyId) {
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
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          }]
        }
      };
      if (excludedSurveyId) {
        // jscs:disable
        query.body.query.bool.must_not = {
          'term': {
            'id': excludedSurveyId
          }
        };
        // jscs:enable
      }
      return ElasticSearchClient.search(query);
    };
    var findByStudyId = function(studyId, selectedAttributes, from, size) {
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
              'studyId': studyId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };

    var findByVariableId = function(variableId, selectedAttributes,
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
              'variables.id': variableId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findByDataSetId = function(dataSetId, selectedAttributes,
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
              'dataSets.id': dataSetId
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

    var findSurveyMethods = function(searchText, filter, language) {
      language = language || LanguageService.getCurrentInstantly();
      var query = createQueryObject();
      var termFilters = createTermFilters(filter);

      query.body = {
        'aggs': {
            'surveyMethodDe': {
                'terms': {
                  'field': 'surveyMethod.de',
                  'size': 100
                },
                'aggs': {
                  'surveyMethodEn': {
                    'terms': {
                      'field': 'surveyMethod.en',
                      'size': 100
                    }
                  }
                }
              }
          }
      };

      query.body.query = {
        'bool': {
          'must': [{
              'match': {
              }
            }],
          'disable_coord': true
        }
      };

      query.body.query.bool.must[0].match
        ['surveyMethod.' + language + '.ngrams'] = {
        'query': searchText,
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      if (termFilters) {
        query.body.query.bool.filter = termFilters;
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var surveyMethods = [];
        var surveyMethodElement = {};
        result.aggregations.surveyMethodDe.buckets.forEach(function(bucket) {
            surveyMethodElement = {
              'de': bucket.key,
              'en': bucket.surveyMethodEn.buckets[0].key
            };
            surveyMethods.push(surveyMethodElement);
          });
        return surveyMethods;
      });
    };

    return {
      findOneById: findOneById,
      findSurveys: findSurveys,
      findByProjectId: findByProjectId,
      findByStudyId: findByStudyId,
      findByDataSetId: findByDataSetId,
      findByVariableId: findByVariableId,
      countBy: countBy,
      findSurveyMethods: findSurveyMethods
    };
  });
