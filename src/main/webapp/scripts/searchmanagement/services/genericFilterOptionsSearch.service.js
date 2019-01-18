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
      var termFilters = createTermFilters(searchConfig.filter,
        searchConfig.dataAcquisitionProjectId, searchConfig.type);
      var fieldName = searchConfig.filterAttribute;
      var prefix = searchConfig.prefix ? searchConfig.prefix + '.' : '';

      query.body = {
        'aggs': {
          'firstLabel': {
            'terms': {
              'field': prefix + fieldName + '.' + language,
              'size': 100
            },
            'aggs': {
              'secondLabel': {
                'terms': {
                  'field': prefix + fieldName +
                    (language === 'de' ? '.en' : '.de'),
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
            'match': {}
          }]
        }
      };

      query.body.query.bool.must[0].match
        [prefix + fieldName + '.' + language + '.ngrams'] = {
        'query': searchConfig.searchText,
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      if (termFilters) {
        query.body.query.bool.filter = termFilters;
      }

      SearchHelperService.addQuery(query, searchConfig.queryTerm);

      return ElasticSearchClient.search(query).then(function(result) {
        var studySeries = [];
        var studySeriesElement = {};
        result.aggregations.firstLabel.buckets.forEach(function(bucket) {
          studySeriesElement = {
            'de': language === 'de' ? bucket.key
              : bucket.secondLabel.buckets[0].key,
            'en': language === 'en' ? bucket.key
              : bucket.secondLabel.buckets[0].key,
            'count': bucket.doc_count
          };
          studySeries.push(studySeriesElement);
        });
        return studySeries;
      });
    };

    return {
      findFilterOptions: findFilterOptions
    };
  });
