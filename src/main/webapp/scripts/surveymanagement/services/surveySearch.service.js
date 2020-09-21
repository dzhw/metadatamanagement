/* global  _*/
'use strict';

angular.module('metadatamanagementApp').factory('SurveySearchService',
  function(ElasticSearchClient, $q, LanguageService, SearchHelperService,
           CleanJSObjectService, GenericFilterOptionsSearchService, Principal) {
      var createQueryObject = function(type) {
        type = type || 'surveys';
        return {
          index: type
        };
      };

      var createTermFilters = function(filter, dataAcquisitionProjectId, type) {
        type = type || 'surveys';
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
        var query = createQueryObject();
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

      var findByDataPackageId = function(dataPackageId, selectedAttributes,
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
                'dataPackageId': dataPackageId
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

      var countBy = function(term, value, version) {
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

        var isAnonymous = Principal.loginName() === null;

        query.body.query.bool.filter.push({term: {
            shadow: isAnonymous
          }});

        if (isAnonymous) {
          if (version) {
            query.body.query.bool.filter.push({
              term: {
                'release.version': version
              }
            });
          } else {
            query.body.query.bool.must_not = [{
              exists: {
                field: 'successorId'
              }
            }];
          }
        }

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
              }],
            'filter': {
              'term': {
                'shadow': false
              }
            }
          }
        };

        query.body.query.bool.must[0].match
          ['surveyMethod.' + language + '.ngrams'] = {
          'query': searchText || '',
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
                      'masterId': {
                        'terms': {
                          'field': prefix + 'masterId',
                          'size': 100
                        }
                      },
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
          'query': searchText || '',
          'operator': 'AND',
          'minimum_should_match': '100%',
          'zero_terms_query': 'ALL'
        };

        SearchHelperService.addNestedShadowCopyFilter(
          aggregation.aggs.title.filter.bool, prefix, type);

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
        if (!_.includes(['related_publications', 'concepts'], type)) {
          SearchHelperService.addShadowCopyFilter(query, filter);
        }

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
                masterId: bucket.masterId.buckets[0].key,
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
        findShadowByIdAndVersion: findShadowByIdAndVersion,
        findByDataPackageId: findByDataPackageId,
        findByDataSetId: findByDataSetId,
        countBy: countBy,
        findSurveyMethods: findSurveyMethods,
        findSurveyTitles: findSurveyTitles,
        findSurveyMethodFilterOptions: findSurveyMethodFilterOptions
      };
    });
