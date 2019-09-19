/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory('StudySearchService',
  function($q, ElasticSearchClient, CleanJSObjectService, SearchHelperService,
           GenericFilterOptionsSearchService, LanguageService) {
    var createQueryObject = function(type) {
      type = type || 'studies';
      return {
        index: type
      };
    };

    var createTermFilters = function(filter, dataAcquisitionProjectId, type) {
      type = type || 'studies';
      var termFilter;
      if (!CleanJSObjectService.isNullOrEmpty(filter) ||
        !CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId)) {
        termFilter = [];
      }
      if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId) &&
        !_.includes(['related_publications', 'concepts'], type)) {
        var projectFilter = {
          term: {
            dataAcquisitionProjectId: dataAcquisitionProjectId
          }
        };
        termFilter.push(projectFilter);
      }
      if (!CleanJSObjectService.isNullOrEmpty(filter)) {
        termFilter = _.concat(termFilter,
          SearchHelperService.createTermFilters(type, filter));
      }
      return termFilter;
    };

    var findOneById = function(id, attributes) {
      var deferred = $q.defer();
      var query = createQueryObject();
      query.id = id;
      query._source = attributes;
      ElasticSearchClient.getSource(query, function(error, response) {
        if (error) {
          deferred.reject(error);
        } else {
          deferred.resolve(response);
        }
      });
      return deferred;
    };

    var findShadowByIdAndVersion = function(id, version) {
      var query = {};
      _.extend(query, createQueryObject(),
        SearchHelperService.createShadowByIdAndVersionQuery(id, version));
      var deferred = $q.defer();
      ElasticSearchClient.search(query).then(function(result) {
        if (result.hits.hits.length === 1) {
          deferred.resolve(result.hits.hits[0]._source);
        } else {
          return deferred.resolve(null);
        }
      }, deferred.reject);
      return deferred;
    };

    var findStudySeries = function(searchText, filter, language, type,
        queryterm, dataAcquisitionProjectId, ignoreAuthorization) {
      ignoreAuthorization = ignoreAuthorization || false;
      language = language || LanguageService.getCurrentInstantly();
      var query = createQueryObject(type);
      query.size = 0;
      var termFilters = createTermFilters(filter, dataAcquisitionProjectId,
        type);
      var fieldName = 'studySeries.';
      var prefix = (type === 'studies' || !type) ? '' : 'study.';
      if (type === 'related_publications') {
        prefix = '';
        fieldName = 'studySerieses.';
      }
      query.body = {
        'aggs': {
          'firstStudySeries': {
            'terms': {
              'field': prefix + fieldName + language,
              'size': 100
            },
            'aggs': {
              'secondStudySeries': {
                'terms': {
                  'field': prefix + fieldName +
                    (language === 'de' ? 'en' : 'de'),
                  'size': 100
                }
              }
            }
          }
        }
      };

      query.body.query = {
        'bool': {
          'must': [{
            'match': {}
          }]
        }
      };

      query.body.query.bool.must[0].match
        [prefix + fieldName + language + '.ngrams'] = {
        'query': searchText || '',
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      if (termFilters) {
        query.body.query.bool.filter = termFilters;
      }

      SearchHelperService.addQuery(query, queryterm);
      if (!_.includes(['related_publications', 'concepts'], type)) {
        SearchHelperService.addShadowCopyFilter(query, filter);
      }

      if (!ignoreAuthorization) {
        SearchHelperService.addFilter(query);
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var studySeries = [];
        var studySeriesElement = {};
        result.aggregations.firstStudySeries.buckets.forEach(function(bucket) {
          studySeriesElement = {
            'de': language === 'de' ? bucket.key
              : bucket.secondStudySeries.buckets[0].key,
            'en': language === 'en' ? bucket.key
              : bucket.secondStudySeries.buckets[0].key,
            'count': bucket.doc_count
          };
          studySeries.push(studySeriesElement);
        });
        return studySeries;
      });
    };

    var findStudyTitles = function(searchText, filter, type,
                                   queryterm, dataAcquisitionProjectId) {
      var language = LanguageService.getCurrentInstantly();
      var query = createQueryObject(type);
      query.size = 0;
      query.body = {};
      var termFilters = createTermFilters(filter, dataAcquisitionProjectId,
        type);
      var prefix = (type === 'studies' || !type) ? ''
        : 'nestedStudy.';
      if (type === 'related_publications' || type === 'concepts') {
        prefix = 'nestedStudies.';
      }
      var aggregation = {
        'aggs': {
          'title': {
            'filter': {
              'bool': {
                'must': [{
                  'match': {}
                }]
              }
            },
            'aggs': {
              'id': {
                'terms': {
                  'field': prefix + 'id',
                  'size': 100
                },
                'aggs': {
                  'masterId': {
                    'terms': {
                      'field': prefix + 'masterId',
                      'size': 100
                    }
                  },
                  'titleDe': {
                    'terms': {
                      'field': prefix + 'title.de',
                      'size': 100
                    },
                    'aggs': {
                      'titleEn': {
                        'terms': {
                          'field': prefix + 'title.en',
                          'size': 100
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      };
      var nestedAggregation = {
        'aggs': {
          'studies': {
            'nested': {
              'path': prefix.replace('.', '')
            }
          }
        }
      };

      aggregation.aggs.title.filter.bool.must[0]
        .match[prefix + 'completeTitle.' + language] = {
        'query': searchText || '',
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      SearchHelperService.addNestedShadowCopyFilter(
        aggregation.aggs.title.filter.bool, prefix, type);

      if (prefix !== '') {
        nestedAggregation.aggs.studies.aggs =
          aggregation.aggs;
        query.body.aggs = nestedAggregation.aggs;
      } else {
        query.body.aggs = aggregation.aggs;
      }

      if (termFilters) {
        query.body.query = query.body.query || {};
        query.body.query.bool = query.body.query.bool || {};
        query.body.query.bool.filter = termFilters;
      }

      SearchHelperService.addQuery(query, queryterm);
      SearchHelperService.addFilter(query);
      if (!_.includes(['related_publications', 'concepts'], type)) {
        SearchHelperService.addShadowCopyFilter(query, filter);
      }
      return ElasticSearchClient.search(query).then(function(result) {
        var titles = [];
        var titleElement = {};
        var buckets = [];
        if (prefix !== '') {
          buckets = result.aggregations.studies.title.id.buckets;
        } else {
          buckets = result.aggregations.title.id.buckets;
        }
        buckets.forEach(function(bucket) {
          titleElement = {
            title: {
              de: bucket.titleDe.buckets[0].key,
              en: bucket.titleDe.buckets[0].titleEn.buckets[0].key
            },
            id: bucket.key,
            masterId: bucket.masterId.buckets[0].key,
            count: bucket.doc_count
          };
          titles.push(titleElement);
        });
        return titles;
      });
    };

    var findSponsors = function(searchText, filter, language,
                                ignoreAuthorization) {
      ignoreAuthorization = ignoreAuthorization || false;
      language = language || LanguageService.getCurrentInstantly();
      var query = createQueryObject();
      var termFilters = createTermFilters(filter);
      query.size = 0;
      query.body = {
        'aggs': {
          'sponsorDe': {
            'terms': {
              'field': 'sponsor.de',
              'size': 100
            },
            'aggs': {
              'sponsorEn': {
                'terms': {
                  'field': 'sponsor.en',
                  'size': 100
                }
              }
            }
          }
        }
      };

      query.body.query = {
        'bool': {
          'must': [{
            'match': {}
          }]
        }
      };

      query.body.query.bool.must[0].match
        ['sponsor.' + language + '.ngrams'] = {
        'query': searchText || '',
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      if (termFilters) {
        query.body.query.bool.filter = termFilters;
      } else {
        query.body.query.bool.filter = [];
      }

      query.body.query.bool.filter.push({
        'term': {
          'shadow': false
        }
      });

      if (!ignoreAuthorization) {
        SearchHelperService.addFilter(query);
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var sponsors = [];
        var sponsorElement = {};
        result.aggregations.sponsorDe.buckets.forEach(function(bucket) {
          sponsorElement = {
            'de': bucket.key,
            'en': bucket.sponsorEn.buckets[0].key
          };
          sponsors.push(sponsorElement);
        });
        return sponsors;
      });
    };

    var findInstitutions = function(searchText, filter, language,
                                    ignoreAuthorization) {
      ignoreAuthorization = ignoreAuthorization || false;
      language = language || LanguageService.getCurrentInstantly();
      var query = createQueryObject();
      var termFilters = createTermFilters(filter);
      query.size = 0;
      query.body = {
        'aggs': {
          'institutionDe': {
            'terms': {
              'field': 'institution.de',
              'size': 100
            },
            'aggs': {
              'institutionEn': {
                'terms': {
                  'field': 'institution.en',
                  'size': 100
                }
              }
            }
          }
        }
      };

      query.body.query = {
        'bool': {
          'must': [{
            'match': {}
          }],
          'filter': {
            'term': {
              'shadow': false
            }
          }
        }
      };

      query.body.query.bool.must[0].match
        ['institution.' + language + '.ngrams'] = {
        'query': searchText || '',
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      if (termFilters) {
        query.body.query.bool.filter = termFilters;
      }

      if (!ignoreAuthorization) {
        SearchHelperService.addFilter(query);
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var institutions = [];
        var institutionElement = {};
        result.aggregations.institutionDe.buckets.forEach(function(bucket) {
          institutionElement = {
            'de': bucket.key,
            'en': bucket.institutionEn.buckets[0].key
          };
          institutionElement.count = bucket.doc_count;
          institutions.push(institutionElement);
        });
        return institutions;
      });
    };

    var buildSearchConfig = function(searchText, filter, type, queryTerm,
        dataAcquisitionProjectId, filterAttribute) {

      return {
        searchText: searchText,
        type: type,
        filter: filter,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        filterAttribute: filterAttribute,
        queryTerm: queryTerm,
        prefix: type === 'studies' ? '' : 'study'
      };
    };

    var findInstitutionFilterOptions = function(searchText, filter, type,
        queryTerm, dataAcquisitionProjectId) {

      var searchConfig = buildSearchConfig(
        searchText,
        filter,
        type,
        queryTerm,
        dataAcquisitionProjectId,
        'institution'
      );

      return GenericFilterOptionsSearchService.findFilterOptions(searchConfig);
    };

    var findSponsorFilterOptions = function(searchText, filter, type,
        queryTerm, dataAcquisitionProjectId) {

      var searchConfig = buildSearchConfig(
        searchText,
        filter,
        type,
        queryTerm,
        dataAcquisitionProjectId,
        'sponsor'
      );

      return GenericFilterOptionsSearchService.findFilterOptions(searchConfig);
    };

    var findTags = function(searchText, language, filter, ignoreAuthorization) {
      ignoreAuthorization = ignoreAuthorization || false;
      language = language || LanguageService.getCurrentInstantly();
      var query = createQueryObject();
      var termFilters = createTermFilters(filter);
      query.size = 0;
      query.body = {
        'aggs': {
          'tags': {
            'terms': {
              'field': 'tags.' + language,
              'size': 100
            }
          }
        }
      };

      query.body.query = {
        'bool': {
          'must': [{
            'match': {}
          }],
          'filter': {
            'term': {
              'shadow': false
            }
          }
        }
      };

      query.body.query.bool.must[0].match
        ['tags.' + language + '.ngrams'] = {
        'query': searchText,
        'operator': 'AND',
        'minimum_should_match': '100%',
        'zero_terms_query': 'ALL'
      };

      if (termFilters) {
        query.body.query.bool.filter = termFilters;
      }

      if (!ignoreAuthorization) {
        SearchHelperService.addFilter(query);
      }

      return ElasticSearchClient.search(query).then(function(result) {
        var tags = [];
        result.aggregations.tags.buckets.forEach(function(bucket) {
          tags.push(bucket.key);
        });
        return tags;
      });
    };

    return {
      findOneById: findOneById,
      findShadowByIdAndVersion: findShadowByIdAndVersion,
      findStudySeries: findStudySeries,
      findSponsors: findSponsors,
      findInstitutions: findInstitutions,
      findStudyTitles: findStudyTitles,
      findInstitutionFilterOptions: findInstitutionFilterOptions,
      findSponsorFilterOptions: findSponsorFilterOptions,
      findTags: findTags
    };
  });
