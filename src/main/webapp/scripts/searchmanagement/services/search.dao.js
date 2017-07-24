/* global _*/
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
  function(ElasticSearchClient, CleanJSObjectService, Principal,
    LanguageService, StudyIdBuilderService, SearchFilterHelperService) {
    var addAdditionalShouldQueries = function(elasticsearchType, queryterm,
      queryShould) {

      //Definition of Boosting for Elasticsearch Search Queries
      var standardMinorBoost = 0.25;
      var germanMajorBoost = 1.0;
      var germanMinorBoost = 0.25;
      var englishMajorBoost = 1.0;
      var englishMinorBoost = 0.25;

      //Change Boosting by Language
      var currentLanguage = LanguageService.getCurrentInstantly();
      switch (currentLanguage) {
        //German is the actual language, decrease english boosting
        case 'de':
          englishMajorBoost = 0.125;
          englishMinorBoost = 0.125;
          break;
        //English is the actual language, decrease german boosting
        case 'en':
          germanMajorBoost = 0.125;
          englishMinorBoost = 0.125;
          break;
      }

      //Add fields with boosting for search of different domain objects
      switch (elasticsearchType) {
        case 'studies':
          queryShould.push({
            'multi_match': {
              'query': queryterm,
              'fields': [
                'title.de^' + germanMajorBoost,
                'title.en^' + englishMajorBoost,
                'authors.firstName',
                'authors.middleName',
                'authors.lastName',
                '_id',
                'description.de^' + germanMinorBoost,
                'description.en^' + englishMinorBoost
              ],
              'type': 'phrase_prefix',
              'operator': 'AND',
              'zero_terms_query': 'NONE',
              'minimum_should_match': 1
            }
          });
          break;

        case 'surveys':
          queryShould.push({
            'multi_match': {
              'query': queryterm,
              'fields': [
                'title.de^' + germanMajorBoost,
                'title.en^' + englishMajorBoost,
                'surveyMethod.de^' + germanMajorBoost,
                'surveyMethod.en^' + englishMajorBoost,
                '_id',
                'population.de^' + germanMinorBoost,
                'population.en^' + englishMinorBoost,
                'sample.de^' + germanMinorBoost,
                'sample.en^' + englishMinorBoost,
              ],
              'type': 'phrase_prefix',
              'operator': 'AND',
              'zero_terms_query': 'NONE',
              'minimum_should_match': 1
            }
          });
          break;

        case 'instruments':
          queryShould.push({
            'multi_match': {
              'query': queryterm,
              'fields': [
                'title.de^' + germanMajorBoost,
                'title.en^' + englishMajorBoost,
                'description.de^' + germanMajorBoost,
                'description.en^' + englishMajorBoost,
                '_id',
                'type^' + standardMinorBoost
              ],
              'type': 'phrase_prefix',
              'operator': 'AND',
              'zero_terms_query': 'NONE',
              'minimum_should_match': 1
            }
          });
          break;

        case 'questions':
          queryShould.push({
            'multi_match': {
              'query': queryterm,
              'fields': [
                'instrument.description.de^' + germanMajorBoost,
                'instrument.description.en^' + englishMajorBoost,
                '_id',
                'type.de^' + germanMajorBoost,
                'type.en^' + englishMajorBoost,
                'questionText.de^' + germanMinorBoost,
                'questionText.en^' + englishMinorBoost
              ],
              'type': 'phrase_prefix',
              'operator': 'AND',
              'zero_terms_query': 'NONE',
              'minimum_should_match': 1
            }
          });
          break;

        case 'data_sets':
          queryShould.push({
            'multi_match': {
              'query': queryterm,
              'fields': [
                'description.de^' + germanMajorBoost,
                'description.en^' + englishMajorBoost,
                '_id',
                'type.de^' + germanMajorBoost,
                'type.en^' + englishMajorBoost,
                'surveys.title.de^' + germanMinorBoost,
                'surveys.title.en^' + englishMinorBoost,
                'accessWays^' + standardMinorBoost
              ],
              'type': 'phrase_prefix',
              'operator': 'AND',
              'zero_terms_query': 'NONE',
              'minimum_should_match': 1
            }
          });
          break;

        case 'variables':
          queryShould.push({
            'multi_match': {
              'query': queryterm,
              'fields': [
                'label.de^' + germanMajorBoost,
                'label.en^' + englishMajorBoost,
                'name^' + standardMinorBoost,
                '_id',
                'dataType.de^' + germanMajorBoost,
                'dataType.en^' + englishMajorBoost,
                'scaleLevel.de^' + germanMajorBoost,
                'scaleLevel.en^' + englishMajorBoost,
                'surveys.title.de^' + germanMinorBoost,
                'surveys.title.en^' + englishMinorBoost
              ],
              'type': 'phrase_prefix',
              'operator': 'AND',
              'zero_terms_query': 'NONE',
              'minimum_should_match': 1
            }
          });
          break;

        case 'related_publications':
          queryShould.push({
            'multi_match': {
              'query': queryterm,
              'fields': [
                'title',
                'doi',
                'publicationAbstract^' + standardMinorBoost,
                '_id'
              ],
              'type': 'phrase_prefix',
              'operator': 'AND',
              'zero_terms_query': 'NONE',
              'minimum_should_match': 1
            }
          });
          break;
      }
    };

    return {
      search: function(queryterm, pageNumber, dataAcquisitionProjectId,
        filter, elasticsearchType, pageSize, sortBy) {
        var query = {};
        var studyId;
        query.index = elasticsearchType;
        if (!elasticsearchType) {
          //search in all indices
          query.index = ['studies', 'variables', 'surveys', 'data_sets',
            'instruments', 'related_publications', 'questions'
          ];
        }
        query.type = elasticsearchType;
        query.body = {};
        query.searchType = 'dfs_query_then_fetch';
        //use source filtering for returning only required attributes
        query.body._source = ['id', 'number', 'questionText', 'title',
          'description', 'type', 'year', 'publicationAbstract', 'authors',
          'surveyMethod', 'fieldPeriod', 'label', 'name', 'dataType',
          'sample', 'indexInInstrument', 'wave',
          'scaleLevel', 'dataAcquisitionProjectId', 'dataSetNumber',
          'population',
          'instrumentNumber', 'instrument.description', 'surveys.title',
          'language', 'subDataSets'
        ];
        if (sortBy && sortBy !== '') {
          var sortCriteria = {};
          sortCriteria[sortBy] = {
            'order': 'asc'
          };
          query.body.sort = [];
          query.body.sort.push(sortCriteria);
        }
        //a query term
        if (!CleanJSObjectService.isNullOrEmpty(queryterm)) {
          query.body.query = {
            'bool': {
              'minimum_number_should_match': 1,
              'should': [
                {
                'match': {
                  '_all': {
                    'query': queryterm,
                    'operator': 'AND',
                    'minimum_should_match': 1,
                    'zero_terms_query': 'NONE',
                    'boost': 0.01 //decrease the ngram results for scoring
                  }
                }
              }
            ]
            }
          };

          addAdditionalShouldQueries(elasticsearchType,
            queryterm, query.body.query.bool.should);

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
                'field': '_type'
              }
            }
          };
        }

        //only publisher see unreleased projects
        if (!Principal.hasAuthority('ROLE_PUBLISHER')) {
          query.body.query.bool.filter = [];
          query.body.query.bool.filter.push({
            'exists': {
              'field': 'release'
            }
          });
        }

        if (dataAcquisitionProjectId) {
          studyId = StudyIdBuilderService
            .buildStudyId(dataAcquisitionProjectId);
          if (!query.body.query.bool.filter) {
            query.body.query.bool.filter = [];
          }

          var boolFilter = {
            'bool': {
              'should': [{
                'term': {
                  'dataAcquisitionProjectId': dataAcquisitionProjectId
                }
              },{
                'term': {
                  'studyIds': studyId
                }
              }]
            }
          };

          query.body.query.bool.filter.push(boolFilter);
        }

        if (!CleanJSObjectService.isNullOrEmpty(filter)) {
          if (!query.body.query.bool.filter) {
            query.body.query.bool.filter = SearchFilterHelperService
            .createTermFilters(elasticsearchType, filter);
          } else {
            query.body.query.bool.filter = _.concat(
              query.body.query.bool.filter, SearchFilterHelperService
              .createTermFilters(elasticsearchType, filter));
          }
        }

        return ElasticSearchClient.search(query);
      }
    };
  });
