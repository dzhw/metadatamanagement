'use strict';

angular.module('metadatamanagementApp').service('VariableSearchDao',
  function(ElasticSearchProperties, Language, ElasticSearchClient,
    CleanJSObjectService) {
    return {
      search: function(queryterm, pageNumber, elasticsearchType) {
        var query = {};
        query.index = 'metadata_' + Language.getCurrentInstantly();
        query.type = elasticsearchType;

        //a query term
        if (!CleanJSObjectService.isNullOrEmpty(queryterm)) {
          query.body = {
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
          };
          //no query term
        } else {
          query.body = {};
          query.body.query = {
            'match_all': {}
          };
        }

        //aggregations if user is on the all tab
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
        return ElasticSearchClient.search(query);
      }
    };
  });
