'use strict';

angular.module('metadatamanagementApp').service('VariableSearchQuerybuilder',
    function($rootScope) {
  return {
    Query: function(queryterm) {
      var query = {
          'index': 'metadata_' + $rootScope.currentLanguage,
          'type': 'variables',
          'body': {
            'query': {
              'match_phrase_prefix': {
                'question': queryterm
              }
            },
            'aggregations': {
              'variableSurvey': {
                'nested': {
                  'path': 'variableSurvey'
                },
                'aggregations': {
                  'variableSurvey.title': {
                    'terms': {
                      'field': 'variableSurvey.title'
                    }
                  }
                }
              },
              'scaleLevel': {
                'terms': {
                  'field': 'scaleLevel'
                }
              }
            }
          }
        };
      if (!queryterm) {
        query.body.query = {
            'match_all': {}
          };
      }
      return query;
    }};
});
