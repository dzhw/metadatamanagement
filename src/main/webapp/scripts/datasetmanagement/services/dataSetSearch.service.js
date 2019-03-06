/* global  _*/
'use strict';

angular.module('metadatamanagementApp').factory('DataSetSearchService',
  function(ElasticSearchClient, $q, CleanJSObjectService, SearchHelperService,
    LanguageService) {
    var createQueryObject = function(type) {
      type = type || 'data_sets';
      return {
        index: type,
        type: type
      };
    };

    var createNestedTermFilters = function(filter, prefix) {
      var result = [];
      if (!prefix || prefix === '' ||
          CleanJSObjectService.isNullOrEmpty(filter)) {
        return result;
      }
      if (filter.survey) {
        var term = {
          term: {}
        };
        term.term[prefix + 'surveyIds'] = filter.survey;
        result.push(term);
      }
      return result;
    };

    var createTermFilters = function(filter, dataAcquisitionProjectId, type) {
      type = type || 'data_sets';
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
          SearchHelperService.createTermFilters(type, filter));
      }
      return termFilter;
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

    var findShadowByIdAndVersion = function(id, version) {
      var query = {};
      _.extend(query, createQueryObject(),
        SearchHelperService.createShadowByIdAndVersionQuery(id, version));
      var deferred = $q.defer();
      ElasticSearchClient.search(query).then(function(result) {
        if (result.hits.total === 1) {
          deferred.resolve(result.hits.hits[0]._source);
        } else {
          return deferred.resolve(null);
        }
      }, deferred.reject);
      return deferred;
    };

    var findOneByVariableId = function(variableId, selectedAttributes) {
      var query =  createQueryObject();
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
    var findByStudyId = function(studyId, selectedAttributes, from, size) {
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
              'studyId': studyId
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

    var countByMultiple = function(map) {
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
      _.forEach(map, function(value, key) {
        var mustTerm = {
          'term': {}
        };
        mustTerm.term[key] = value;
        query.body.query.bool.filter.push(mustTerm);
      });
      return ElasticSearchClient.count(query);
    };

    var findDataSetDescriptions = function(searchText, filter, type,
      queryterm, dataAcquisitionProjectId) {
      var language = LanguageService.getCurrentInstantly();
      var query = createQueryObject(type);
      query.size = 0;
      query.body = {};
      var termFilters = createTermFilters(filter, dataAcquisitionProjectId,
        type);
      var prefix = (type === 'data_sets' || !type)  ? ''
        : 'nestedDataSets.';
      if (type === 'variables') {
        prefix = 'nestedDataSet.';
      }
      var aggregation = {
        'aggs': {
          'description': {
            'filter': {
              'bool': {
                'must': [{
                  'match': {
                  }
                }]
              }
            },
            'aggs': {
              'id': {
                'terms': {
                  'field': prefix + 'id',
                  'size': 100
                },
                'aggs': {
                  'masterId': {
                    'terms': {
                      'field': prefix + 'masterId',
                      'size': 100
                    }
                  },
                  'descriptionDe': {
                    'terms': {
                      'field': prefix + 'description.de',
                      'size': 100
                    },
                    'aggs': {
                      'descriptionEn': {
                        'terms': {
                          'field': prefix + 'description.en',
                          'size': 100
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      };
      var nestedAggregation = {
        'aggs': {
          'dataSets': {
            'nested': {
              'path': prefix.replace('.', '')
            }
          }
        }
      };

      aggregation.aggs.description.filter.bool.must[0]
        .match[prefix + 'completeTitle.' + language] =  {
          'query': searchText,
          'operator': 'AND',
          'minimum_should_match': '100%',
          'zero_terms_query': 'ALL'
        };

      if (termFilters) {
        query.body.query = query.body.query || {};
        query.body.query.bool = query.body.query.bool || {};
        query.body.query.bool.filter = termFilters;

        aggregation.aggs.description.filter.bool.must = _.concat(
          aggregation.aggs.description.filter.bool.must,
          createNestedTermFilters(filter, prefix));
      }

      if (prefix !== '') {
        nestedAggregation.aggs.dataSets.aggs =
        aggregation.aggs;
        query.body.aggs = nestedAggregation.aggs;
      } else {
        query.body.aggs = aggregation.aggs;
      }

      SearchHelperService.addQuery(query, queryterm);
      SearchHelperService.addFilter(query);
      if (type !== 'related_publications') {
        SearchHelperService.addShadowCopyFilter(query, _.isEmpty(filter));
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var descriptions = [];
        var descriptionElement = {};
        var buckets = [];
        if (prefix !== '') {
          buckets = result.aggregations.dataSets.description.id.buckets;
        } else {
          buckets = result.aggregations.description.id.buckets;
        }
        buckets.forEach(function(bucket) {
          descriptionElement = {
            description: {
              de: bucket.descriptionDe.buckets[0].key,
              en: bucket.descriptionDe.buckets[0].descriptionEn.buckets[0].key
            },
            id: bucket.key,
            masterId: bucket.masterId.buckets[0].key,
            count: bucket.doc_count
          };
          descriptions.push(descriptionElement);
        });
        return descriptions;
      });
    };

    var findAccessWays = function(term, filter, dataAcquisitionProjectId,
      queryterm) {
      var query = createQueryObject();
      var termFilters = createTermFilters(filter, dataAcquisitionProjectId);
      query.size = 0;
      query.body = {
        'aggs': {
            'accessWays': {
                'terms': {
                  'field': 'accessWays',
                  'include': '.*' + term.toLowerCase() + '.*',
                  'size': 100,
                  'order': {
                    '_key': 'asc'
                  }
                }
              }
          }
      };

      if (termFilters) {
        query.body.query = {
          bool: {
          }
        };
        query.body.query.bool.filter = termFilters;
      }

      SearchHelperService.addQuery(query, queryterm);

      SearchHelperService.addFilter(query);

      return ElasticSearchClient.search(query).then(function(result) {
        return result.aggregations.accessWays.buckets;
      });
    };

    return {
      findOneById: findOneById,
      findOneByVariableId: findOneByVariableId,
      findBySurveyId: findBySurveyId,
      findByProjectId: findByProjectId,
      findByStudyId: findByStudyId,
      countBy: countBy,
      countByMultiple: countByMultiple,
      findShadowByIdAndVersion: findShadowByIdAndVersion,
      findDataSetDescriptions: findDataSetDescriptions,
      findAccessWays: findAccessWays
    };
  });
