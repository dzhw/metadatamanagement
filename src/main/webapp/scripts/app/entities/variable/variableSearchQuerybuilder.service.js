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
              'match_phrase_prefix': {
                'label': queryterm
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
    }};
});
