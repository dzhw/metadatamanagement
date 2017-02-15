'use strict';

angular.module('metadatamanagementApp').factory('StudySearchService',
  function(ElasticSearchClient) {
    var query = {};
    query.type = 'studies';
    query.index = 'studies';

    var findStudies = function(studyIds, selectedAttributes) {
      query.body = {};
      query.body.query = {};
      query.body._source = selectedAttributes;
      query.body.query.docs = {
        'ids': studyIds
      };
      return ElasticSearchClient.mget(query);
    };
    var findStudy = function(studyId, selectedAttributes) {
      query.body = {};
      query.body._source = selectedAttributes;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'id': studyId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    var findOneByProjectId = function(dataAcquisitionProjectId,
      selectedAttributes) {
      query.body = {};
      query.body.size = 1;
      query.body._source = selectedAttributes;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };
    return {
      findStudies: findStudies,
      findStudy: findStudy,
      findOneByProjectId: findOneByProjectId
    };
  });
