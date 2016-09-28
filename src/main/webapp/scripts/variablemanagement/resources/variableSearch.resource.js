'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'variables';
  query.body = {};

  var findByQuestionId = function(questionId) {
    query.filterPath = 'hits.hits._source';
    query.body.query = {
      'bool': {
        'must': [
          {
            'match_all': {}
          }
        ],
        'filter': [
          {
            'term': {
              'questionId': questionId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  return {
    findByQuestionId: findByQuestionId
  };
});
