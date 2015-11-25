'use strict';

angular.module('metadatamanagementApp').service('VariableSearchQuerybuilder',
    function($rootScope) {
      return {
        Query: function(queryterm, page) {
          var query = {
            'index': 'metadata_' + $rootScope.currentLanguage,
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
              'from': page,
              'size': 3
            }
          };
          if (!queryterm) {
            query.body.query = {
              'match_all': {}
            };
          }
          return query;
        }
      };
    });
