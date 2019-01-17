/* global  _*/
'use strict';

angular.module('metadatamanagementApp').factory('SurveySearchService',
  function(ElasticSearchClient, $q, LanguageService, SearchHelperService,
           CleanJSObjectService, GenericFilterOptionsSearchService) {
      var createQueryObject = function(type) {
        type = type || 'surveys';
        return {
          index: type,
          type: type
        };
      };

      var createTermFilters = function(filter, dataAcquisitionProjectId, type) {
        type = type || 'surveys';
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

      var findSurveyMethods = function(searchText, filter, language,
        ignoreAuthorization) {
        ignoreAuthorization = ignoreAuthorization || false;
        language = language || LanguageService.getCurrentInstantly();
        var query = createQueryObject();
        var termFilters = createTermFilters(filter);
        query.size = 0;
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
              }]
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

        if (!ignoreAuthorization) {
          SearchHelperService.addFilter(query);
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

      var findSurveyTitles = function(searchText, filter, type,
        queryterm, dataAcquisitionProjectId) {
        var language = LanguageService.getCurrentInstantly();
        var query = createQueryObject(type);
        query.size = 0;
        query.body = {};
        var termFilters = createTermFilters(filter, dataAcquisitionProjectId,
          type);
        var prefix = (type === 'surveys' || !type)  ? ''
          : 'nestedSurveys.';
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
                      'titleDe': {
                        'terms': {
                          'field': prefix + 'title.de',
                          'size': 100
                        },
                        'aggs': {
                          'titleEn': {
                            'terms': {
                              'field': prefix + 'title.en',
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
              'surveys': {
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

        if (prefix !== '') {
          nestedAggregation.aggs.surveys.aggs =
            aggregation.aggs;
          query.body.aggs = nestedAggregation.aggs;
        } else {
          query.body.aggs = aggregation.aggs;
        }

        if (termFilters) {
          query.body.query = query.body.query || {};
          query.body.query.bool = query.body.query.bool || {};
          query.body.query.bool.filter = termFilters;
        }

        SearchHelperService.addQuery(query, queryterm);

        SearchHelperService.addFilter(query);

        return ElasticSearchClient.search(query).then(function(result) {
          var titles = [];
          var titleElement = {};
          var buckets = [];
          if (prefix !== '') {
            buckets = result.aggregations.surveys.title.id.buckets;
          } else {
            buckets = result.aggregations.title.id.buckets;
          }
          buckets.forEach(function(bucket) {
              titleElement = {
                title: {
                  de: bucket.titleDe.buckets[0].key,
                  en: bucket.titleDe.buckets[0].titleEn.buckets[0].key
                },
                id: bucket.key,
                count: bucket.doc_count
              };
              titles.push(titleElement);
            });
          return titles;
        });
      };

      var findSurveyMethodFilterOptions = function(searchText, filter, type,
        queryTerm, dataAcquisitionProjectId) {

        return GenericFilterOptionsSearchService.findFilterOptions({
          searchText: searchText,
          type: type,
          filter: filter,
          dataAcquisitionProjectId: dataAcquisitionProjectId,
          filterAttribute: 'surveyMethod',
          queryTerm: queryTerm,
          prefix: type === 'surveys' ? '' : 'surveys'
        });
      };

      return {
        findOneById: findOneById,
        findByStudyId: findByStudyId,
        findByDataSetId: findByDataSetId,
        countBy: countBy,
        findSurveyMethods: findSurveyMethods,
        findSurveyTitles: findSurveyTitles,
        findSurveyMethodFilterOptions: findSurveyMethodFilterOptions
      };
    });
