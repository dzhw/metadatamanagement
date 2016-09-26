'use strict';

angular.module('metadatamanagementApp').factory('SurveySearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'surveys';
  query.body = {};
  var findSurveys = function(surveyIds) {
    query.filterPath = 'docs._source';
    query.body.query = {};
    query.body.query.docs = {
      'ids': surveyIds
    };
    return ElasticSearchClient.mget(query);
  };
  return {
    findSurveys: findSurveys
  };
});
