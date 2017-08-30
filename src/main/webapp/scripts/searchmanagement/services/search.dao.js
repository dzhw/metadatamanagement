/* global _, ClientJS*/
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
  function(ElasticSearchClient, CleanJSObjectService, Principal,
    LanguageService, StudyIdBuilderService, SearchFilterHelperService) {
    //use a fake sessionId for consistent shard routing
    var fingerprint = new ClientJS().getFingerprint();

    var addAdditionalShouldQueries = function(elasticsearchType, query,
      boolQuery) {
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
            'query': {
              'match': {}
            },
            'boost': boost
          }
        };
        // jscs:disable requireCamelCaseOrUpperCaseIdentifiers
        constantScoreQuery.constant_score.query.match[fieldName] = {
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
          case 'studies':
            boolQuery.should.push(createConstantScoreQuery(
              'title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyDesign.de.ngrams', queryTerm, germanSuperBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyDesign.en.ngrams', queryTerm, englishSuperBoost));
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
            break;

          case 'surveys':
            boolQuery.should.push(createConstantScoreQuery(
              'title.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'title.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyMethod.de.ngrams', queryTerm, germanMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'surveyMethod.en.ngrams', queryTerm, englishMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'id.ngrams', queryTerm, standardMajorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'population.de.ngrams', queryTerm, germanMinorBoost));
            boolQuery.should.push(createConstantScoreQuery(
              'population.en.ngrams', queryTerm, englishMinorBoost));
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
              'publicationAbstract.ngrams', queryTerm, standardMinorBoost));
            break;
        }
      });
    };

    return {
      search: function(queryterm, pageNumber, dataAcquisitionProjectId,
        filter, elasticsearchType, pageSize, sortBy) {
        var query = {};
        query.preference = fingerprint;
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
                'constant_score': {
                  'query': {
                    'match': {
                      '_all': {
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
