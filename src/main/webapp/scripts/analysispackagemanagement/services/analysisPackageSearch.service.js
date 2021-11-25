/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .factory('AnalysisPackageSearchService',
    function($q, ElasticSearchClient, CleanJSObjectService, SearchHelperService,
             GenericFilterOptionsSearchService, LanguageService,
             AnalysisPackageIdBuilderService) {
      var createQueryObject = function(type) {
        type = type || 'analysis_packages';
        return {
          index: type
        };
      };

      var createTermFilters = function(filter, dataAcquisitionProjectId, type) {
        type = type || 'analysis_packages';
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
        if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProjectId) &&
          type === 'related_publications') {
          var analysisPackageId = AnalysisPackageIdBuilderService
            .buildAnalysisPackageId(
              dataAcquisitionProjectId);
          var analysisPackageFilter = {
            term: {
              'analysisPackages.id': analysisPackageId
            }
          };
          termFilter.push(analysisPackageFilter);
        }
        if (!CleanJSObjectService.isNullOrEmpty(filter)) {
          termFilter = _.concat(termFilter,
            SearchHelperService.createTermFilters(type, filter));
        }
        return termFilter;
      };

      var findOneById = function(id, attributes, excludes) {
        var deferred = $q.defer();
        var query = createQueryObject();
        query.id = id;
        if (attributes) {
          query._source = attributes;
        }
        if (excludes) {
          query._source_excludes = excludes.join(',');
        }
        ElasticSearchClient.getSource(query, function(error, response) {
          if (error) {
            deferred.reject(error);
          } else {
            deferred.resolve(response);
          }
        });
        return deferred;
      };

      var findShadowByIdAndVersion = function(id, version, excludes) {
        var query = {};
        _.extend(query, createQueryObject(),
          SearchHelperService.createShadowByIdAndVersionQuery(id, version));
        if (excludes) {
          query.body._source = {
            'excludes': excludes
          };
        }
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

      var findAnalysisPackageTitles = function(searchText, filter, type,
                                               queryterm,
                                               dataAcquisitionProjectId) {
        var language = LanguageService.getCurrentInstantly();
        var query = createQueryObject(type);
        query.size = 0;
        query.body = {};
        var termFilters = createTermFilters(filter, dataAcquisitionProjectId,
          type);
        var prefix = (type === 'analysis_packages' || !type) ? ''
          : 'nestedAnalysisPackage.';
        if (type === 'related_publications' || type === 'concepts') {
          prefix = 'nestedAnalysisPackages.';
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
            'analysisPackages': {
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
          nestedAggregation.aggs.dataPackages.aggs =
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
            buckets = result.aggregations.analysisPackages.title.id.buckets;
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
                                  ignoreAuthorization, excludedSponsors) {
        ignoreAuthorization = ignoreAuthorization || false;
        language = language || LanguageService.getCurrentInstantly();
        var query = createQueryObject();
        var termFilters = createTermFilters(filter);
        query.size = 0;
        query.body = {
          'aggs': {
            'sponsors': {
              'nested': {
                'path': 'nestedSponsors'
              },
              'aggs': {
                'filtered': {
                  'filter': {
                    'bool': {
                      'must': [{
                        'match': {}
                      }]
                    }
                  },
                  'aggs': {
                    'sponsorDe': {
                      'terms': {
                        'field': 'nestedSponsors.de',
                        'size': 100
                      },
                      'aggs': {
                        'sponsorEn': {
                          'terms': {
                            'field': 'nestedSponsors.en',
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

        query.body.query = {
          'bool': {
            'filter': {
              'term': {
                'shadow': false
              }
            }
          }
        };

        query.body.aggs.sponsors.aggs.filtered.filter.bool.must[0].match
          ['nestedSponsors.' + language + '.ngrams'] = {
          'query': searchText || '',
          'operator': 'AND',
          'minimum_should_match': '100%',
          'zero_terms_query': 'ALL'
        };

        if (excludedSponsors && excludedSponsors.length > 0) {
          query.body.aggs.sponsors.aggs.filtered.filter.bool.must_not = [];
          excludedSponsors.forEach(function(sponsor) {
            if (sponsor) {
              query.body.aggs.sponsors.aggs.filtered.filter.bool.must_not
                .push({
                  'term': {
                    'nestedSponsors.de': sponsor.de
                  }
                });
            }
          });
        }

        if (termFilters) {
          query.body.query.bool.filter = termFilters;
        }

        if (!ignoreAuthorization) {
          SearchHelperService.addFilter(query);
        }

        return ElasticSearchClient.search(query).then(function(result) {
          var sponsors = [];
          var sponsorElement = {};
          result.aggregations.sponsors.filtered.sponsorDe.buckets.forEach(
            function(bucket) {
              sponsorElement = {
                'de': bucket.key,
                'en': bucket.sponsorEn.buckets[0].key
              };
              sponsorElement.count = bucket.doc_count;
              sponsors.push(sponsorElement);
            });
          return sponsors;
        });
      };

      var findInstitutions = function(searchText, filter, language,
                                      ignoreAuthorization,
                                      excludedInstitutions) {
        ignoreAuthorization = ignoreAuthorization || false;
        language = language || LanguageService.getCurrentInstantly();
        var query = createQueryObject();
        var termFilters = createTermFilters(filter);
        query.size = 0;
        query.body = {
          'aggs': {
            'institutions': {
              'nested': {
                'path': 'nestedInstitutions'
              },
              'aggs': {
                'filtered': {
                  'filter': {
                    'bool': {
                      'must': [{
                        'match': {}
                      }]
                    }
                  },
                  'aggs': {
                    'institutionDe': {
                      'terms': {
                        'field': 'nestedInstitutions.de',
                        'size': 100
                      },
                      'aggs': {
                        'institutionEn': {
                          'terms': {
                            'field': 'nestedInstitutions.en',
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

        query.body.query = {
          'bool': {
            'filter': {
              'term': {
                'shadow': false
              }
            }
          }
        };

        query.body.aggs.institutions.aggs.filtered.filter.bool.must[0].match
          ['nestedInstitutions.' + language + '.ngrams'] = {
          'query': searchText || '',
          'operator': 'AND',
          'minimum_should_match': '100%',
          'zero_terms_query': 'ALL'
        };

        if (excludedInstitutions && excludedInstitutions.length > 0) {
          query.body.aggs.institutions.aggs.filtered.filter.bool.must_not = [];
          excludedInstitutions.forEach(function(institution) {
            if (institution) {
              query.body.aggs.institutions.aggs.filtered.filter.bool.must_not
                .push({
                  'term': {
                    'nestedInstitutions.de': institution.de
                  }
                });
            }
          });
        }

        if (termFilters) {
          query.body.query.bool.filter = termFilters;
        }

        if (!ignoreAuthorization) {
          SearchHelperService.addFilter(query);
        }

        return ElasticSearchClient.search(query).then(function(result) {
          var institutions = [];
          var institutionElement = {};
          result.aggregations.institutions.filtered.institutionDe.buckets
            .forEach(
              function(bucket) {
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
                                       dataAcquisitionProjectId,
                                       filterAttribute) {

        return {
          searchText: searchText,
          type: type,
          filter: filter,
          dataAcquisitionProjectId: dataAcquisitionProjectId,
          filterAttribute: filterAttribute,
          queryTerm: queryTerm,
          prefix: type === 'analysis_packages' ? '' : 'analysisPackage'
        };
      };

      var findInstitutionFilterOptions = function(searchText, filter, type,
                                                  queryTerm,
                                                  dataAcquisitionProjectId) {

        var searchConfig = buildSearchConfig(
          searchText,
          filter,
          type,
          queryTerm,
          dataAcquisitionProjectId,
          'institutions'
        );

        return GenericFilterOptionsSearchService
          .findFilterOptions(searchConfig);
      };

      var findSponsorFilterOptions = function(searchText, filter, type,
                                              queryTerm,
                                              dataAcquisitionProjectId) {

        var searchConfig = buildSearchConfig(
          searchText,
          filter,
          type,
          queryTerm,
          dataAcquisitionProjectId,
          'sponsors'
        );

        return GenericFilterOptionsSearchService
          .findFilterOptions(searchConfig);
      };

      var findTags = function(searchText, language, filter,
                              ignoreAuthorization) {
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
        findSponsors: findSponsors,
        findInstitutions: findInstitutions,
        findAnalysisPackageTitles: findAnalysisPackageTitles,
        findInstitutionFilterOptions: findInstitutionFilterOptions,
        findSponsorFilterOptions: findSponsorFilterOptions,
        findTags: findTags
      };
    });
