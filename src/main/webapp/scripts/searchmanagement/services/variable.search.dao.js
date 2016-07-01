'use strict';

angular.module('metadatamanagementApp').service('VariableSearchDao',
  function(ElasticSearchProperties, Language, ElasticSearchClient,
    CleanJSObjectService) {
    return {
      search: function(queryterm, pageNumber, elasticsearchType) {
        var query = {
          'index': 'metadata_' + Language.getCurrentInstantly(),
          'type': elasticsearchType,
          'body': {
            'query': {
              'bool': {
                'should': [{
                  'match': {
                    '_all': {
                      'query': queryterm,
                      'type': 'boolean',
                      'operator': 'AND',
                      'zero_terms_query': 'NONE'
                    }
                  }
                }, {
                  'match': {
                    'allStringsAsNgrams': {
                      'query': queryterm,
                      'type': 'boolean',
                      'operator': 'AND',
                      'minimum_should_match': '100%',
                      'zero_terms_query': 'NONE'
                    }
                  }
                }]
              }
            },
            'filter': {
              'term': {
                '_type': 'variables'
              }
            },
            'from': (pageNumber - 1) *
              ElasticSearchProperties.pageSize,
            'size': ElasticSearchProperties.pageSize
          }
        };
        if (!queryterm) {
          query.body.query = {
            'match_all': {}
          };
          if (CleanJSObjectService.isNullOrEmpty(elasticsearchType)) {
            query.body.aggs = {
              'countVariables': {
                'filter': {
                  'term': {
                    '_type': 'variables'
                  }
                }
              }
            };
          }
        }
        return ElasticSearchClient.search(query);
      }
    };
  });
