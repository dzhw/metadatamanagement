'use strict';

angular.module('metadatamanagementApp').factory('StudySearchService',
  function(ElasticSearchClient, $q) {
    var query = {};
    query.type = 'studies';
    query.index = 'studies';

    var findOneById = function(id) {
      var deferred = $q.defer();
      delete query.body;
      query.id = id;
      ElasticSearchClient.getSource(query, function(error, response) {
          if (error) {
            deferred.reject(error);
          } else {
            deferred.resolve(response);
          }
        });
      return deferred;
    };
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
      findOneById: findOneById,
      findStudies: findStudies,
      findStudy: findStudy,
      findOneByProjectId: findOneByProjectId
    };
  });
