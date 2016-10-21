'use strict';

angular.module('metadatamanagementApp')
.factory('RelatedPublicationSearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'data_sets';
  query.body = {};

  var findBySurveyId = function(surveyId) {
    query.filterPath = '';
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
              'surveyIds': surveyId
            }
          }
        ]
      }
    };
    return ElasticSearchClient.search(query);
  };
  return {
    findBySurveyId: findBySurveyId
  };
});
