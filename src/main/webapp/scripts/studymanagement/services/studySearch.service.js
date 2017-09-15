/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory('StudySearchService',
  function(ElasticSearchClient, $q, CleanJSObjectService,
    SearchFilterHelperService) {
    var createQueryObject = function() {
      return {
        index: 'studies',
        type: 'studies'
      };
    };

    var createTermFilters = function(filter, dataAcquisitionProjectId) {
      var termFilter;
      if (!CleanJSObjectService.isNullOrEmpty(filter) ||
        !CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        termFilter = [];
      }
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        var projectFilter = {
          term: {
            dataAcquisitionProjectId: dataAcquisitionProjectId
          }
        };
        termFilter.push(projectFilter);
      }
      if (!CleanJSObjectService.isNullOrEmpty(filter)) {
        termFilter = _.concat(termFilter,
          SearchFilterHelperService.createTermFilters('studies', filter));
      }
      return termFilter;
    };

    var findOneById = function(id) {
      var deferred = $q.defer();
      var query = createQueryObject();
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
      var query = createQueryObject();
      query.body = {};
      query.body.query = {};
      query.body._source = selectedAttributes;
      query.body.query.docs = {
        'ids': studyIds
      };
      return ElasticSearchClient.mget(query);
    };
    var findStudy = function(studyId, selectedAttributes) {
      var query = createQueryObject();
      query.body = {};
      query.body._source = selectedAttributes;
      query.body.query = {
        'bool': {
          'must': [{
            'match_all': {}
          }],
          'filter': [{
            'term': {
              '_id': studyId
            }
          }]
        }
      };
      return ElasticSearchClient.search(query);
    };

    var findOneByProjectId = function(dataAcquisitionProjectId,
      selectedAttributes) {
      var query = createQueryObject();
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

    var findSurveySeries = function(filter,
      dataAcquisitionProjectId) {
      var query = createQueryObject();
      var termFilters = createTermFilters(filter, dataAcquisitionProjectId);

      query.body = {
        'size': 0,
        'aggs': {
            'surveySeriesDe': {
                'terms': {
                  'field': 'surveySeries.de',
                  'include': '.*.*'
                },
                'aggs': {
                  'surveySeriesEn': {
                    'terms': {
                      'field': 'surveySeries.en',
                      'include': '.*.*'
                    }
                  }
                }
              }
          }
      };

      if (termFilters) {
        query.body.query = {
          bool: {
          }
        };
        query.body.query.bool.filter = termFilters;
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var surveySeries = _.map([result.aggregations.surveySeriesDe.buckets],
          function(buckets) {
            var bucket;
            var surveySeriesArray = [];
            for (bucket of buckets) {
              var surveySeriesElement = {
                'de': bucket.key,
                'en': bucket.surveySeriesEn.buckets[0].key
              };
              surveySeriesArray.push(surveySeriesElement);
            }
            return surveySeriesArray;
          });
        return surveySeries;
      });
    };

    return {
      findOneById: findOneById,
      findStudies: findStudies,
      findStudy: findStudy,
      findOneByProjectId: findOneByProjectId,
      findSurveySeries: findSurveySeries
    };
  });
