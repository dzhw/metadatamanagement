/* globals _ */
'use strict';

angular.module('metadatamanagementApp')
  .service('GenericFilterOptionsSearchService',
  function(SearchHelperService, LanguageService, ElasticSearchClient,
           CleanJSObjectService) {

    var createTermFilters = function(filter, dataAcquisitionProjectId, type) {
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

    var findFilterOptions = function(searchConfig) {
      var type = searchConfig.type;

      var language = LanguageService.getCurrentInstantly();

      var query = {
        index: type,
        type: type
      };

      query.size = 0;
      query.body = {};

      var termFilters = createTermFilters(searchConfig.filter,
        searchConfig.dataAcquisitionProjectId, type);

      var prefix = searchConfig.prefix ? searchConfig.prefix + '.' : '';

      var aggregationFilterFieldName = prefix + searchConfig.filterAttribute +
        '.' + language;

      var aggregation = {
        'aggs': {
          'label': {
            'filter': {
              'bool': {
                'must': [{
                  'match': {}
                }]
              }
            },
            'aggs': {
              'labelDE': {
                'terms': {
                  'field': prefix + searchConfig.filterAttribute + '.de',
                  'size': 100
                },
                'aggs': {
                  'labelEN': {
                    'terms': {
                      'field': prefix + searchConfig.filterAttribute + '.en',
                      'size': 100
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
          'labelAggregation': {
            'nested': {
              'path': searchConfig.prefix
            }
          }
        }
      };

      aggregation.aggs.label.filter.bool.must[0]
        .match[aggregationFilterFieldName + '.ngrams'] = {
        'query': searchConfig.searchText,
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      if (termFilters) {
        _.set(query, 'body.query.bool.filter', termFilters);
      }

      if (prefix !== '') {
        nestedAggregation.aggs.labelAggregation.aggs =
          aggregation.aggs;
        query.body.aggs = nestedAggregation.aggs;
      } else {
        query.body.aggs = aggregation.aggs;
      }

      SearchHelperService.addQuery(query, searchConfig.queryTerm);

      SearchHelperService.addFilter(query);

      return ElasticSearchClient.search(query).then(function(result) {
        var labelElement = {};
        var labels = [];
        var buckets = [];

        if (prefix !== '') {
          buckets = result.aggregations.labelAggregation.label.labelDE.buckets;
        } else {
          buckets = result.aggregations.label.labelDE.buckets;
        }

        buckets.forEach(function(bucket) {
          labelElement = {
            de: bucket.key,
            en: bucket.labelEN.buckets[0].key,
            count: bucket.doc_count
          };
          labels.push(labelElement);
        });
        return labels;
      });
    };

    return {
      findLabels: findFilterOptions
    };
  });
