/* global _*/
'use strict';

angular.module('metadatamanagementApp').factory('QuestionSearchService',
  function(ElasticSearchClient, $q, CleanJSObjectService, SearchHelperService,
    LanguageService) {
    var createQueryObject = function(type) {
      type = type || 'questions';
      return {
        index: type
      };
    };

    var createNestedTermFilters = function(filter, prefix) {
      var result = [];
      if (!prefix || prefix === '' ||
          CleanJSObjectService.isNullOrEmpty(filter)) {
        return result;
      }
      if (filter.instrument) {
        var term = {
          term: {}
        };
        term.term[prefix + 'instrumentId'] = filter.instrument;
        result.push(term);
      }
      return result;
    };

    var createTermFilters = function(filter, dataAcquisitionProjectId, type) {
      type = type || 'questions';
      var termFilter;
      if (!CleanJSObjectService.isNullOrEmpty(filter) ||
        !CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        termFilter = [];
      }
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId) &&
        !_.includes(['related_publications', 'concepts'], type)) {
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

    var findOneById = function(id, attributes, excludes) {
      var deferred = $q.defer();
      var query =  createQueryObject();
      query.id = id;
      if (attributes) {
        query._source = attributes;
      }
      if (excludes) {
        query._source_excludes = excludes.join(',');
      }
      ElasticSearchClient.getSource(query, function(error, response) {
          if (error) {
            deferred.reject(error);
          } else {
            deferred.resolve(response);
          }
        });
      return deferred;
    };

    var findShadowByIdAndVersion = function(id, version, excludes) {
      var query = {};
      _.extend(query, createQueryObject(),
        SearchHelperService.createShadowByIdAndVersionQuery(id, version));
      if (excludes) {
        query.body._source = {
          'excludes': excludes
        };
      }
      var deferred = $q.defer();
      ElasticSearchClient.search(query).then(function(result) {
        if (result.hits.hits.length === 1) {
          deferred.resolve(result.hits.hits[0]._source);
        } else {
          return deferred.resolve(null);
        }
      }, deferred.reject);
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

    var findQuestionTitles = function(searchText, filter, type,
      queryterm, dataAcquisitionProjectId) {
      var language = LanguageService.getCurrentInstantly();
      var query = createQueryObject(type);
      query.size = 0;
      query.body = {};
      var termFilters = createTermFilters(filter, dataAcquisitionProjectId,
        type);
      var prefix = (type === 'questions' || !type)  ? ''
        : 'nestedQuestions.';
      var aggregation = {
          'aggs': {
            'title': {
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
                    'number': {
                      'terms': {
                        'field': prefix + 'number',
                        'size': 100
                      },
                      'aggs': {
                        'textDe': {
                          'terms': {
                            'field': prefix + 'questionText.de',
                            'size': 100
                          }
                        },
                        'textEn': {
                          'terms': {
                            'field': prefix + 'questionText.en',
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
            'questions': {
              'nested': {
                'path': prefix.replace('.', '')
              }
            }
          }
      };

      aggregation.aggs.title.filter.bool.must[0]
      .match[prefix + 'completeTitle.' + language] =  {
        'query': searchText || '',
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      if (termFilters) {
        query.body.query = query.body.query || {};
        query.body.query.bool = query.body.query.bool || {};
        query.body.query.bool.filter = termFilters;

        aggregation.aggs.title.filter.bool.must = _.concat(
          aggregation.aggs.title.filter.bool.must,
          createNestedTermFilters(filter, prefix));
      }

      SearchHelperService.addNestedShadowCopyFilter(
        aggregation.aggs.title.filter.bool, prefix, type);

      if (prefix !== '') {
        nestedAggregation.aggs.questions.aggs =
          aggregation.aggs;
        query.body.aggs = nestedAggregation.aggs;
      } else {
        query.body.aggs = aggregation.aggs;
      }

      SearchHelperService.addQuery(query, queryterm);
      SearchHelperService.addFilter(query);
      if (!_.includes(['related_publications', 'concepts'], type)) {
        SearchHelperService.addShadowCopyFilter(query, filter);
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var titles = [];
        var titleElement = {};
        var buckets = [];
        if (prefix !== '') {
          buckets = result.aggregations.questions.title.id.buckets;
        } else {
          buckets = result.aggregations.title.id.buckets;
        }
        buckets.forEach(function(bucket) {
            var numberBucket = bucket.number.buckets[0];
            titleElement = {
              questionText: {
                de: numberBucket.textDe.buckets[0] ?
                  numberBucket.textDe.buckets[0].key : '',
                en: numberBucket.textEn.buckets[0] ?
                  numberBucket.textEn.buckets[0].key : ''
              },
              number: numberBucket.key,
              id: bucket.key,
              masterId: bucket.masterId.buckets[0].key,
              count: bucket.doc_count
            };
            titles.push(titleElement);
          });
        return titles;
      });
    };
    return {
      findOneById: findOneById,
      findAllPredeccessors: findAllPredeccessors,
      findAllSuccessors: findAllSuccessors,
      findByVariableId: findByVariableId,
      findShadowByIdAndVersion: findShadowByIdAndVersion,
      findQuestionTitles: findQuestionTitles
    };
  });
