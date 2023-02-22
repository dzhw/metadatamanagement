/* global _ */
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
  function(ElasticSearchClient, CleanJSObjectService, Principal,
           LanguageService, DataPackageIdBuilderService,
           AnalysisPackageIdBuilderService,
           SearchHelperService, clientId) {

    var addAdditionalShouldQueries = function(elasticsearchType, query,
                                              boolQuery) {
      console.log("additional should queries search term", query);
      var queryTerms = query.split(' ');
      if (CleanJSObjectService.isNullOrEmpty(boolQuery.should)) {
        boolQuery.should = [];
      }

      //Definition of Boosting for Elasticsearch Search Queries
      var standardSuperBoost = 10;
      var standardMajorBoost = 1;
      var standardMinorBoost = 0.1;
      var germanSuperBoost = 10;
      var germanMajorBoost = 1;
      var germanMinorBoost = 0.1;
      var englishSuperBoost = 10;
      var englishMajorBoost = 1;
      var englishMinorBoost = 0.1;

      //Change Boosting by Language
      var currentLanguage = LanguageService.getCurrentInstantly();
      switch (currentLanguage) {
        //German is the actual language, decrease english boosting
        case 'de':
          englishSuperBoost = 0.01;
          englishMajorBoost = 0.001;
          englishMinorBoost = 0.0001;
          break;
        //English is the actual language, decrease german boosting
        case 'en':
          germanSuperBoost = 0.01;
          germanMajorBoost = 0.001;
          germanMinorBoost = 0.0001;
          break;
      }

      var createConstantScoreQuery = function(fieldName, queryTerm, boost,
                                              lenient) {
        var constantScoreQuery = {
          'constant_score': {
            'filter': {
              'match': {}
            },
            'boost': boost
          }
        };
        // jscs:disable requireCamelCaseOrUpperCaseIdentifiers
        constantScoreQuery.constant_score.filter.match[fieldName] = {
          'query': queryTerm,
          'operator': 'and',
          'minimum_should_match': '100%',
          'lenient': lenient === true
        };
        // jscs:enable requireCamelCaseOrUpperCaseIdentifiers
        return constantScoreQuery;
      };
      //Add fields with boosting for search of different domain objects
      queryTerms.forEach(function(queryTerm) {
        switch (elasticsearchType) {
          case 'data_packages':
            boolQuery.should.push(createConstantScoreQuery(
              'title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyDesign.de.ngrams', queryTerm, germanSuperBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyDesign.en.ngrams', queryTerm, englishSuperBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyDataTypes.de.ngrams', queryTerm, germanSuperBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyDataTypes.en.ngrams', queryTerm, englishSuperBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'accessWays.ngrams', queryTerm, standardSuperBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'projectContributors.firstName.ngrams',
              queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'projectContributors.middleName.ngrams',
              queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'projectContributors.lastName.ngrams',
              queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'description.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'description.en.ngrams', queryTerm, englishMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'tags.de', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'tags.en', queryTerm, englishMajorBoost));
            break;

          case 'analysis_packages':
            boolQuery.should.push(createConstantScoreQuery(
              'title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'authors.firstName.ngrams',
              queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'authors.middleName.ngrams',
              queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'authors.lastName.ngrams',
              queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'description.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'description.en.ngrams', queryTerm, englishMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'tags.de', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'tags.en', queryTerm, englishMajorBoost));
            break;

          case 'surveys':
            boolQuery.should.push(createConstantScoreQuery(
              'title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'dataType.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'dataType.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyMethod.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyMethod.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'population.description.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'population.description.en.ngrams',
              queryTerm, englishMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'sample.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'sample.en.ngrams', queryTerm, englishMinorBoost));
            break;

          case 'instruments':
            boolQuery.should.push(createConstantScoreQuery(
              'description.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'description.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'type.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'title.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'title.en.ngrams', queryTerm, englishMinorBoost));
            break;

          case 'questions':
            boolQuery.should.push(createConstantScoreQuery(
              'instrument.description.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'instrument.description.en.ngrams', queryTerm, englishMajorBoost)
            );
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'number.edge_ngrams', queryTerm, standardSuperBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'type.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'type.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'questionText.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'questionText.en.ngrams', queryTerm, englishMinorBoost));
            break;

          case 'data_sets':
            boolQuery.should.push(createConstantScoreQuery(
              'description.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'description.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'type.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'type.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveys.title.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveys.title.en.ngrams', queryTerm, englishMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'subDataSets.accessWays.ngrams', queryTerm, standardMinorBoost));
            break;

          case 'variables':
            boolQuery.should.push(createConstantScoreQuery(
              'label.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'label.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'concepts.tags.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'name.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'dataType.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'dataType.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'scaleLevel.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'scaleLevel.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveys.title.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveys.title.en.ngrams', queryTerm, englishMinorBoost));
            break;

          case 'related_publications':
            boolQuery.should.push(createConstantScoreQuery(
              'title.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'authors.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'year', queryTerm, standardSuperBoost, true));
            boolQuery.should.push(createConstantScoreQuery(
              'sourceReference.ngrams', queryTerm, standardMinorBoost));
            break;

          case 'concepts':
            boolQuery.should.push(createConstantScoreQuery(
              'title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'authors.firstName.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'authors.middleName.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'authors.lastName.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'description.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'description.en.ngrams', queryTerm, englishMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'tags.de', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'tags.en', queryTerm, englishMajorBoost));
            break;
        }
      });
    };

    var applyFetchDataWhereUserIsDataProviderFilter = function(query,
      elasticSearchType) {
      if (_.includes(['related_publications', 'concepts'], elasticSearchType)) {
        return;
      }
      var loginName = Principal.loginName();

      if (loginName) {
        var filterCriteria = {
          'bool': {
            'must': [{
              'term': {'configuration.dataProviders': loginName}
            }]
          }
        };

        // modify the filter for searching over all indices
        if (!elasticSearchType) {
          filterCriteria = {
            'bool': {
              'should': [{
                'term': {'configuration.dataProviders': loginName}
              }, {
                'term': {'_index': 'concepts'}
              }, {
                'term': {'_index': 'related_publications'}
              }]
            }
          };
        }

        var filterArray = _.get(query, 'body.query.bool.filter');

        if (_.isArray(filterArray)) {
          query.body.query.bool.filter.push(filterCriteria);
        } else {
          _.set(query, 'body.query.bool.filter[0]', filterCriteria);
        }
      }
    };

    var applyFetchLatestShadowCopyFilter = function(query, elasticSearchType,
                                                    filter, enforceReleased) {
      if (!_.includes(['related_publications', 'concepts'],
        elasticSearchType)) {
        SearchHelperService.addShadowCopyFilter(query, filter, enforceReleased);
      }
    };

    /**
     * Function stripping the version number from a filter value.
     * This is only relevant for queries for related publications and concepts.

     * @param {*} filter object with filter name as key and a value
     * @returns {*} filter object with value without version
     */
    var stripVersionSuffixFromFilters = function(filter) {
      var strippedFilter = {};
      _.forEach(_.keys(filter), function(index) {

        var tempFilterValue;
        // in queries from the sidebar
        // filter[index] will be an array with one element
        if (Array.isArray(filter[index])) {
          tempFilterValue = filter[index][0].toString();
        } else {
          // queries from the search filter panel will not be an array
          tempFilterValue = filter[index].toString();
        }
        var match = tempFilterValue.match(/-[0-9]+\.[0-9]+\.[0-9]+$/);
        if (match !== null) {
          strippedFilter[index] = tempFilterValue.substr(0, match.index);
        } else {
          strippedFilter[index] = tempFilterValue;
        }
      });
      return strippedFilter;
    };

    var createAdditionalSearchQueryForPublicUsers = function(queryterm,
      elasticsearchType) {
      var query = {};
      query.index = elasticsearchType;
      query.body = {};
      query.body.track_total_hits = true;
      query.body._source = ['id'];
      query.body.from = 0;
      //define size
      query.body.size = 0;
      //a query term
      if (!CleanJSObjectService.isNullOrEmpty(queryterm)) {
        query.body.query = {
          'bool': {
            'must': [
              {
                'constant_score': {
                  'filter': {
                    'match': {
                      'all': {
                        'query': queryterm,
                        'operator': 'AND',
                        'minimum_should_match': '100%',
                        'zero_terms_query': 'NONE',
                        'boost': 1 //constant base score of 1 for matches
                      }
                    }
                  }
                }
              }
            ]
          }
        };
        //no query term
      } else {
        query.body.query = {
          'bool': {
            'must': [{
              'match_all': {}
            }]
          }
        };
      }
      query.body.query.bool.filter = [];
      query.body.query.bool.filter.push({
        'exists': {
          'field': 'release'
        }
      });
      applyFetchLatestShadowCopyFilter(query, elasticsearchType);
      return query;
    };

    return {
      /**
       * Function creating elastic search query based on input parameters and returning request results.

       * @param {*} queryterm the term to search for
       * @param {*} pageNumber the current page number
       * @param {*} dataAcquisitionProjectId the id of the currently selected project
       * @param {*} filter active filters from the sidebar or filter panel selection
       * @param {*} elasticsearchType the type of data to search for (e.g. concepts)
       * @param {*} pageSize the number of results per page
       * @param {*} idsToExclude ids that should be excluded from search
       * @param {*} aggregations a list of fields aggregations should be created for
       * @param {*} newFilters filter objects created from the url parameters
       * @param {*} sortCriteria option for sorting
       * @param {*} enforceReleased true if search applies only to released data else false
       * @param {*} additionalSearchIndex an array of strings with the names of additional indices (relevant for public user search)
       * @returns {*} seach result
       */
      search: function(queryterm, pageNumber, dataAcquisitionProjectId,
                       filter, elasticsearchType, pageSize, idsToExclude,
                       aggregations, newFilters, sortCriteria,
                       enforceReleased, additionalSearchIndex) {
        var query = {};
        query.preference = clientId;
        var dataPackageId;
        var analysisPackageId;

        query.index = elasticsearchType;
        if (!elasticsearchType) {
          //search in all indices
          query.index = ['data_packages', 'analysis_packages', 'variables',
            'surveys', 'data_sets', 'instruments', 'related_publications',
            'questions', 'concepts'
          ];
        }
        query.body = {};
        query.body.track_total_hits = true;
        //use source filtering for returning only required attributes
        query.body._source = ['id', 'number', 'questionText', 'title',
          'description', 'type', 'year', 'sourceReference', 'authors',
          'surveyMethod', 'fieldPeriod', 'label', 'name', 'dataType',
          'sample', 'shadow', 'projectContributors',
          'scaleLevel', 'dataAcquisitionProjectId', 'dataSetNumber',
          'population', 'release',
          'instrumentNumber', 'instrument.description', 'surveys.title',
          'language', 'subDataSets', 'accessWays', 'maxNumberOfObservations',
          'masterId', 'surveyDataTypes', 'surveyDesign', 'surveyPeriod'
        ];

        query.body.sort = SearchHelperService
          .createSortByCriteria(elasticsearchType, sortCriteria);

        //a query term
        if (!CleanJSObjectService.isNullOrEmpty(queryterm)) {
          query.body.query = {
            'bool': {
              'must': [
                {
                  'constant_score': {
                    'filter': {
                      'match': {
                        'all': {
                          'query': queryterm,
                          'operator': 'AND',
                          'minimum_should_match': '100%',
                          'zero_terms_query': 'NONE',
                          'boost': 1 //constant base score of 1 for matches
                        }
                      }
                    }
                  }
                }
              ]
            }
          };

          addAdditionalShouldQueries(elasticsearchType, queryterm,
            query.body.query.bool);

          //no query term
        } else {
          query.body.query = {
            'bool': {
              'must': [{
                'match_all': {}
              }]
            }
          };
        }

        if (idsToExclude) {
          // jscs:disable
          query.body.query.bool.must_not = {
            'terms': {
              'id': idsToExclude
            }
          };
          // jscs:enable
        }
        //define from
        query.body.from = (pageNumber - 1) * pageSize;
        //define size
        query.body.size = pageSize;
        //aggregations if user is on the all tab
        if (CleanJSObjectService.isNullOrEmpty(elasticsearchType)) {
          //define aggregations
          query.body.aggs = {
            'countByType': {
              'terms': {
                'field': '_index'
              }
            }
          };
        }

        //only publisher and data provider see unreleased projects
        if (enforceReleased || !Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER',
            'ROLE_ADMIN'])) {
          query.body.query.bool.filter = [];
          query.body.query.bool.filter.push({
            'exists': {
              'field': 'release'
            }
          });
        }

        var filterToUse;

        // strip version number from filter values for publications & concepts
        if (_.includes(['related_publications', 'concepts'],
          elasticsearchType)) {
          filterToUse = stripVersionSuffixFromFilters(filter);
        } else {
          filterToUse = filter;
        }

        if (dataAcquisitionProjectId &&
          !SearchHelperService.containsDomainObjectFilter(filterToUse)) {
          dataPackageId = DataPackageIdBuilderService
            .buildDataPackageId(dataAcquisitionProjectId);
          analysisPackageId = AnalysisPackageIdBuilderService
            .buildAnalysisPackageId(dataAcquisitionProjectId);
          if (!query.body.query.bool.filter) {
            query.body.query.bool.filter = [];
          }

          var boolFilter = {
            'bool': {
              'should': [{
                'term': {
                  'dataAcquisitionProjectId': dataAcquisitionProjectId
                }
              }, {
                'term': {
                  'dataPackageIds': dataPackageId
                }
              }, {
                'term': {
                  'dataPackages.id': dataPackageId
                }
              }, {
                'term': {
                  'analysisPackageIds': analysisPackageId
                }
              }, {
                'term': {
                  'analysisPackages.id': analysisPackageId
                }
              }]
            }
          };

          query.body.query.bool.filter.push(boolFilter);
        }

        if (!CleanJSObjectService.isNullOrEmpty(filterToUse)) {
          if (!query.body.query.bool.filter) {
            query.body.query.bool.filter = SearchHelperService
              .createTermFilters(elasticsearchType, filterToUse);
          } else {
            query.body.query.bool.filter = _.concat(
              query.body.query.bool.filter, SearchHelperService
                .createTermFilters(elasticsearchType, filterToUse));
          }
        }

        SearchHelperService.addAggregations(query, elasticsearchType,
          aggregations);

        SearchHelperService.addNewFilters(query, elasticsearchType,
          newFilters);

        // Bestellansicht -> m_search mit enforceRelease = true
        if (!Principal.isProviderActive()){
          applyFetchLatestShadowCopyFilter(query, elasticsearchType,
            filterToUse, true);
          // additionalSearchIndex is a list with one or more indices
          // queries are created and added for each of them
          if (additionalSearchIndex && additionalSearchIndex.length > 0) {
            var header1 = {
              index: query.index,
              preference: query.preference
            };

            var bodylist = [];
            bodylist.push(header1);
            bodylist.push(query.body);

            for (var i = 0; i < additionalSearchIndex.length; i++) {
              var index = additionalSearchIndex[i];
              var additionalQuery = createAdditionalSearchQueryForPublicUsers(
                queryterm, index);
              var additionalHeader = {
                index: additionalQuery.index,
                preference: additionalQuery.preference
              };
              bodylist.push(additionalHeader);
              bodylist.push(additionalQuery.body);
            }
            return ElasticSearchClient.msearch({
              body: bodylist
            });
          } else {
            return ElasticSearchClient.search(query);
          }
          
        // Provideransicht
        } else {
          // only assigned data
          if (!Principal.showAllData()){
            applyFetchDataWhereUserIsDataProviderFilter(query, elasticsearchType);
            applyFetchLatestShadowCopyFilter(query, elasticsearchType,
              filterToUse);
              return ElasticSearchClient.search(query);
          // Publisher & Admin sehen alles (siehe PrincipalService)
          } else { // TODO enforeRelease?????
            applyFetchLatestShadowCopyFilter(query, elasticsearchType,
              filterToUse, enforceReleased);
              return ElasticSearchClient.search(query);
          }
        }
      }
    };
  });
