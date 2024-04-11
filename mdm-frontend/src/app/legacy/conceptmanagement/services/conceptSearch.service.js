/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory('ConceptSearchService', [
  '$q', 'ElasticSearchClient', 'SearchHelperService', 'CleanJSObjectService', 'LanguageService', 
  function($q, ElasticSearchClient, SearchHelperService, CleanJSObjectService,
    LanguageService) {
    var createQueryObject = function(type) {
      type = type || 'concepts';
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
      if (filter.survey) {
        var term = {
          term: {}
        };
        term.term[prefix + 'surveyIds'] = filter.survey;
        result.push(term);
      }
      return result;
    };

    var createTermFilters = function(filter, type) {
      type = type || 'concepts';
      var termFilter;
      if (!CleanJSObjectService.isNullOrEmpty(filter)) {
        termFilter = [];
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

    var findTitles = function(searchText, filter, type,
      queryterm) {
      var language = LanguageService.getCurrentInstantly();
      var query = createQueryObject(type);
      query.size = 0;
      query.body = {};
      var termFilters = createTermFilters(filter,
        type);
      var prefix = (type === 'concepts' || !type)  ? ''
        : 'nestedConcepts.';
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
          'concepts': {
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

      if (prefix !== '') {
        nestedAggregation.aggs.concepts.aggs =
        aggregation.aggs;
        query.body.aggs = nestedAggregation.aggs;
      } else {
        query.body.aggs = aggregation.aggs;
      }

      SearchHelperService.addQuery(query, queryterm);
      SearchHelperService.addFilter(query);
      if (!_.includes(['related_publications', 'concepts'], type)) {
        SearchHelperService.addShadowCopyFilter(query, _.isEmpty(filter));
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var titles = [];
        var titleElement = {};
        var buckets = [];
        if (prefix !== '') {
          buckets = result.aggregations.concepts.title.id.buckets;
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

    var findTags = function(searchText, language, filter, ignoreAuthorization) {
      var query = {
        index: 'concepts',
        size: 0
      };
      _.set(query, 'body.aggs.tags.terms.field', 'tags.' + language);
      _.set(query, 'body.aggs.tags.terms.size', 100);
      _.set(query, 'body.query.bool.must[0].match', {});
      _.set(query, 'body.query.bool.filter.term.shadow', false);
      query.body.query.bool.must[0].match['tags.' + language + '.ngrams'] = {
        'query': searchText || '',
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      var filters = createTermFilters(filter);
      if (filters) {
        _.set(query, 'body.query.bool.filter', filters);
      }

      if (!ignoreAuthorization) {
        SearchHelperService.addFilter(query);
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var tags = [];
        result.aggregations.tags.buckets.forEach(function(bucket) {
          tags.push(bucket.key);
        });
        return tags;
      });
    };

    return {
      findTitles: findTitles,
      findOneById: findOneById,
      findTags: findTags
    };
  }]);

