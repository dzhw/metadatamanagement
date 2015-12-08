'use strict';

angular.module('metadatamanagementApp').service('VariableSearchDao',
    function(Language, ElasticSearchClient) {
      return {
        search: function(queryterm, pageNumber) {
          var query = {
            'index': 'metadata_' + Language.getCurrentInstantly(),
            'type': 'variables',
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
              'from': pageNumber,
              'size': 10
            }
          };
          if (!queryterm) {
            query.body.query = {
              'match_all': {}
            };
          }
          return ElasticSearchClient.search(query);
        }
      };
    });
