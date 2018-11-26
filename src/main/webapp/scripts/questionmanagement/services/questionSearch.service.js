/* global _*/
'use strict';

angular.module('metadatamanagementApp').factory('QuestionSearchService',
  function(ElasticSearchClient, $q, CleanJSObjectService, SearchHelperService,
    LanguageService) {
    var createQueryObject = function(type) {
      type = type || 'questions';
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
                    'textDe': {
                      'terms': {
                        'field': prefix + 'questionText.de',
                        'size': 100
                      },
                      'aggs': {
                        'textEn': {
                          'terms': {
                            'field': prefix + 'questionText.en',
                            'size': 100
                          },
                          'aggs': {
                            'number': {
                              'terms': {
                                'field': prefix + 'number',
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
        'query': searchText,
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

      if (prefix !== '') {
        nestedAggregation.aggs.questions.aggs =
          aggregation.aggs;
        query.body.aggs = nestedAggregation.aggs;
      } else {
        query.body.aggs = aggregation.aggs;
      }

      SearchHelperService.addQuery(query, queryterm);

      SearchHelperService.addFilter(query);

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
            titleElement = {
              questionText: {
                de: bucket.textDe.buckets[0].key,
                en: bucket.textDe.buckets[0].textEn.buckets[0].key
              },
              number: bucket.textDe.buckets[0].textEn.buckets[0]
                .number.buckets[0].key,
              id: bucket.key,
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
      findByProjectId: findByProjectId,
      findByStudyId: findByProjectId,
      findByVariableId: findByVariableId,
      countBy: countBy,
      findQuestionTitles: findQuestionTitles
    };
  });
