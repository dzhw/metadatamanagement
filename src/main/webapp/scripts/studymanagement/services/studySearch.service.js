/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory('StudySearchService',
  function(ElasticSearchClient, $q, CleanJSObjectService,
    SearchFilterHelperService, LanguageService) {
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
          SearchFilterHelperService.createTermFilters('studies', filterDe));
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

    var findSurveySeries = function(searchTermCurrentLanguage,
      searchTermAnotherLanguage, filter, dataAcquisitionProjectId) {

      var query = createQueryObject();
      var termFilters = createTermFilters(filter,
        dataAcquisitionProjectId);
      var currentLanguage = LanguageService.getCurrentInstantly();
      var searchTermDe;
      var searchTermEn;

      if (currentLanguage === 'de') {
        searchTermDe = searchTermCurrentLanguage;
        searchTermEn = searchTermAnotherLanguage;
      } else {
        searchTermEn = searchTermCurrentLanguage;
        searchTermDe = searchTermAnotherLanguage;
      }

      query.body = {
        'size': 0,
        'aggs': {
            'surveySeriesDe': {
                'terms': {
                  'field': 'surveySeries.de',
                  'include': '.*' + searchTermDe + '.*'
                },
                'aggs': {
                  'surveySeriesEn': {
                    'terms': {
                      'field': 'surveySeries.en',
                      'include': '.*' + searchTermEn + '.*'
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
        var surveySeries = [];
        var surveySeriesElement = {};
        result.aggregations.surveySeriesDe.buckets.forEach(function(bucket) {
            surveySeriesElement = {
              'de': bucket.key,
              'en': bucket.surveySeriesEn.buckets[0].key
            };
            surveySeries.push(surveySeriesElement);
          });
        return surveySeries;
      });
    };

    return {
      findOneById: findOneById,
      findSurveySeries: findSurveySeries
    };
  });
