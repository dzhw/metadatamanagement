/* global _*/
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
  function(ElasticSearchClient, CleanJSObjectService, Principal,
    LanguageService, StudyIdBuilderService, SearchFilterHelperService) {
    var addAdditionalShouldQueries = function(elasticsearchType, query,
      boolQuery) {
      var queryTerms = query.split(' ');
      if (CleanJSObjectService.isNullOrEmpty(boolQuery.should)) {
        boolQuery.should = [];
      }

      //Definition of Boosting for Elasticsearch Search Queries
      var standardSuperBoost = 10;
      var standardMajorBoost = 1;
      var standardMinorBoost = 0.25;
      var germanMajorBoost = 1;
      var germanMinorBoost = 0.25;
      var englishMajorBoost = 1;
      var englishMinorBoost = 0.25;

      //Change Boosting by Language
      var currentLanguage = LanguageService.getCurrentInstantly();
      switch (currentLanguage) {
        //German is the actual language, decrease english boosting
        case 'de':
          englishMajorBoost = 0.125;
          englishMinorBoost = 0.0625;
          break;
        //English is the actual language, decrease german boosting
        case 'en':
          germanMajorBoost = 0.125;
          germanMinorBoost = 0.0625;
          break;
      }

      //Add fields with boosting for search of different domain objects
      queryTerms.forEach(function(queryterm) {
        switch (elasticsearchType) {
          case 'studies':
            boolQuery.should.push({
              'multi_match': {
                'query': queryterm,
                'type': 'most_fields',
                'operator': 'and',
                'minimum_should_match': '100%',
                'fields': [
                  'title.de.ngrams^' + germanMajorBoost,
                  'title.en.ngrams^' + englishMajorBoost,
                  'surveyDesign.de.ngrams^' + germanMajorBoost,
                  'surveyDesign.en.ngrams^' + englishMajorBoost,
                  'authors.firstName.ngrams^' + standardMajorBoost,
                  'authors.middleNam.ngrams^' + standardMajorBoost,
                  'authors.lastName.ngrams^' + standardMajorBoost,
                  'id.ngrams^' + standardMajorBoost,
                  'description.de.ngrams^' + germanMinorBoost,
                  'description.en.ngrams^' + englishMinorBoost
                ],
              }
            });
            break;

          case 'surveys':
            boolQuery.should.push({
              'multi_match': {
                'query': queryterm,
                'type': 'most_fields',
                'operator': 'and',
                'minimum_should_match': '100%',
                'fields': [
                  'title.de.ngrams^' + germanMajorBoost,
                  'title.en.ngrams^' + englishMajorBoost,
                  'surveyMethod.de.ngrams^' + germanMajorBoost,
                  'surveyMethod.en.ngrams^' + englishMajorBoost,
                  'id.ngrams^' + standardMajorBoost,
                  'population.de.ngrams^' + germanMinorBoost,
                  'population.en.ngrams^' + englishMinorBoost,
                  'sample.de.ngrams^' + germanMinorBoost,
                  'sample.en.ngrams^' + englishMinorBoost,
                ]
              }
            });
            break;

          case 'instruments':
            boolQuery.should.push({
              'multi_match': {
                'query': queryterm,
                'type': 'most_fields',
                'operator': 'and',
                'minimum_should_match': '100%',
                'fields': [
                  'description.de.ngrams^' + germanMajorBoost,
                  'description.en.ngrams^' + englishMajorBoost,
                  'id.ngrams^' + standardMajorBoost,
                  'type.ngrams^' + standardMajorBoost,
                  'title.de.ngrams^' + germanMinorBoost,
                  'title.en.ngrams^' + englishMinorBoost
                ]
              }
            });
            break;

          case 'questions':
            boolQuery.should.push({
              'multi_match': {
                'query': queryterm,
                'type': 'most_fields',
                'operator': 'and',
                'minimum_should_match': '100%',
                'fields': [
                  'instrument.description.de.ngrams^' + germanMajorBoost,
                  'instrument.description.en.ngrams^' + englishMajorBoost,
                  'id.ngrams^' + standardMajorBoost,
                  'number.edge_ngrams^' + standardSuperBoost,
                  'type.de.ngrams^' + germanMajorBoost,
                  'type.en.ngrams^' + englishMajorBoost,
                  'questionText.de.ngrams^' + germanMinorBoost,
                  'questionText.en.ngrams^' + englishMinorBoost
                ]
              }
            });
            break;

          case 'data_sets':
            boolQuery.should.push({
              'multi_match': {
                'query': queryterm,
                'type': 'most_fields',
                'operator': 'and',
                'minimum_should_match': '100%',
                'fields': [
                  'description.de.ngrams^' + germanMajorBoost,
                  'description.en.ngrams^' + englishMajorBoost,
                  'id.ngrams^' + standardMajorBoost,
                  'type.de.ngrams^' + germanMajorBoost,
                  'type.en.ngrams^' + englishMajorBoost,
                  'surveys.title.de.ngrams^' + germanMinorBoost,
                  'surveys.title.en.ngrams^' + englishMinorBoost,
                  'subDataSets.accessWays.ngrams ^' + standardMinorBoost
                ]
              }
            });
            break;

          case 'variables':
            boolQuery.should.push({
              'multi_match': {
                'query': queryterm,
                'type': 'most_fields',
                'operator': 'and',
                'minimum_should_match': '100%',
                'fields': [
                  'label.de.ngrams^' + germanMajorBoost,
                  'label.en.ngrams^' + englishMajorBoost,
                  'name.ngrams^' + standardMajorBoost,
                  'id.ngrams^' + standardMajorBoost,
                  'dataType.de.ngrams^' + germanMajorBoost,
                  'dataType.en.ngrams^' + englishMajorBoost,
                  'scaleLevel.de.ngrams^' + germanMajorBoost,
                  'scaleLevel.en.ngrams^' + englishMajorBoost,
                  'surveys.title.de.ngrams^' + germanMinorBoost,
                  'surveys.title.en.ngrams^' + englishMinorBoost
                ]
              }
            });
            break;

          case 'related_publications':
            boolQuery.should.push({
              'multi_match': {
                'query': queryterm,
                'type': 'most_fields',
                'operator': 'and',
                'minimum_should_match': '100%',
                'lenient': true,
                'fields': [
                  'title.ngrams^' + standardMajorBoost,
                  'authors.ngrams^' + standardMajorBoost,
                  'year^' + standardSuperBoost,
                  'id.ngrams^' + standardMajorBoost,
                  'publicationAbstract.ngrams^' + standardMinorBoost,
                ]
              }
            });
            break;
        }
      });
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
          'sample', 'wave',
          'scaleLevel', 'dataAcquisitionProjectId', 'dataSetNumber',
          'population',
          'instrumentNumber', 'instrument.description', 'surveys.title',
          'language', 'subDataSets'
        ];
        if (sortBy && sortBy !== '') {
          query.body.sort = [];
          sortBy.split(',').forEach(function(fieldName) {
            var sortCriteria = {};
            sortCriteria[fieldName] = {
              'order': 'asc'
            };
            query.body.sort.push(sortCriteria);
          });
        }
        //a query term
        if (!CleanJSObjectService.isNullOrEmpty(queryterm)) {
          query.body.query = {
            'bool': {
              'must': [
                {
                'match': {
                  '_all': {
                    'query': queryterm,
                    'operator': 'AND',
                    'minimum_should_match': '100%',
                    'zero_terms_query': 'NONE',
                    'boost': 0.001 //decrease the ngram results for scoring
                  }
                }
              }
            ],
              'disable_coord': true
            }
          };

          addAdditionalShouldQueries(elasticsearchType,
            queryterm, query.body.query.bool);

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
