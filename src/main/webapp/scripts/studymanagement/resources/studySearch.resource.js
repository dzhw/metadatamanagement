'use strict';

angular.module('metadatamanagementApp').factory('StudySearchResource',
function(Language, ElasticSearchClient) {
  var query = {};
  query.index = 'metadata_' + Language.getCurrentInstantly();
  query.type = 'studies';
  query.body = {};

  var findStudies = function(studyIds) {
    query.body.query = {};
    query.body.query.docs = {
      'ids': studyIds
    };
    return ElasticSearchClient.mget(query);
  };
  return {
    findStudies: findStudies
  };
});
