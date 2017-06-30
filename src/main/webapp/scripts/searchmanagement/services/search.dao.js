/* global _*/
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
  function(ElasticSearchClient, CleanJSObjectService, Principal,
    LanguageService, StudyIdBuilderService) {
    var keyMapping = {
      'studies': {
        'related-publication': 'relatedPublications.id'
      },
      'variables': {
        'data-set': 'dataSetId',
        'panel-identifier': 'panelIdentifier',
        'question': 'relatedQuestions.questionId',
        'related-publication': 'relatedPublications.id',
        'access-way': 'accessWays',
        'study': 'studyId'
      },
      'surveys': {
        'instrument': 'instruments.id',
        'variable': 'variables.id',
        'data-set': 'dataSets.id',
        'question': 'questions.id',
        'related-publication': 'relatedPublications.id',
        'study': 'studyId'
      },
      'questions': {
        'instrument': 'instrumentId',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id',
        'study': 'studyId'
      },
      'instruments': {
        'survey': 'surveyIds',
        'related-publication': 'relatedPublications.id',
        'study': 'studyId'
      },
      'data_sets': {
        'survey': 'surveyIds',
        'related-publication': 'relatedPublications.id',
        'study': 'studyId'
      },
      'related_publications': {
        'variable': 'variableIds',
        'data-set': 'dataSetIds',
        'survey': 'surveyIds',
        'instrument': 'instrumentIds',
        'study': 'studyIds',
        'question': 'questionIds'
      }
    };

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
            'match': {
              'title.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'title.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'authors.firstName': {
                'query': queryterm
              }
            }
          });
          queryShould.push({
            'match': {
              'authors.middleName': {
                'query': queryterm
              }
            }
          });
          queryShould.push({
            'match': {
              'authors.lastName': {
                'query': queryterm
              }
            }
          });
          queryShould.push({
            'match': {
              'description.de': {
                'query': queryterm,
                boost: germanMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'description.en': {
                'query': queryterm,
                boost: englishMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              '_id': queryterm
            }
          });
          break;

        case 'surveys':
          queryShould.push({
            'match': {
              'title.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'title.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              '_id': queryterm
            }
          });
          queryShould.push({
            'match': {
              'surveyMethod.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'surveyMethod.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'population.de': {
                'query': queryterm,
                boost: germanMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'population.en': {
                'query': queryterm,
                boost: englishMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'sample.de': {
                'query': queryterm,
                boost: germanMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'sample.en': {
                'query': queryterm,
                boost: englishMinorBoost
              }
            }
          });
          break;

        case 'instruments':
          queryShould.push({
            'match': {
              'title.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'title.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'description.de': {
                'query': queryterm,
                boost: germanMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'description.en': {
                'query': queryterm,
                boost: englishMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'type': {
                'query': queryterm
              }
            }
          });
          queryShould.push({
            'match': {
              '_id': queryterm
            }
          });
          break;

        case 'questions':
          queryShould.push({
            'match': {
              'instrument.description.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'instrument.description.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'type.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'type.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'questionText.de': {
                'query': queryterm,
                boost: germanMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'questionText.en': {
                'query': queryterm,
                boost: englishMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              '_id': queryterm
            }
          });
          break;

        case 'data_sets':
          queryShould.push({
            'match': {
              'description.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'description.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              '_id': queryterm
            }
          });
          queryShould.push({
            'match': {
              'type.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'type.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'surveys.title.de': {
                'query': queryterm,
                boost: germanMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'surveys.title.en': {
                'query': queryterm,
                boost: englishMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'accessWays': {
                'query': queryterm,
                boost: standardMinorBoost
              }
            }
          });
          break;

        case 'variables':
          queryShould.push({
            'match': {
              'label.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'label.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              '_id': queryterm
            }
          });
          queryShould.push({
            'match': {
              'name': {
                'query': queryterm
              }
            }
          });
          queryShould.push({
            'match': {
              'dataType.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'dataType.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'scaleLevel.de': {
                'query': queryterm,
                boost: germanMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'scaleLevel.en': {
                'query': queryterm,
                boost: englishMajorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'surveys.title.de': {
                'query': queryterm,
                boost: germanMinorBoost
              }
            }
          });
          queryShould.push({
            'match': {
              'surveys.title.en': {
                'query': queryterm,
                boost: englishMinorBoost
              }
            }
          });
          break;

        case 'related_publications':
          queryShould.push({
            'match': {
              'title': {
                'query': queryterm
              }
            }
          });
          queryShould.push({
            'match': {
              '_id': queryterm
            }
          });
          queryShould.push({
            'match': {
              'doi': {
                'query': queryterm
              }
            }
          });
          queryShould.push({
            'match': {
              'publicationAbstract': {
                'query': queryterm,
                boost: standardMinorBoost
              }
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
          'sample',
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
              'should': [{
                'match': {
                  '_all': {
                    'query': queryterm,
                    'operator': 'AND',
                    'minimum_should_match': '100%',
                    'zero_terms_query': 'NONE',
                    'boost': 0.01 //decrease the ngram results for scoring
                  }
                }
              }],
              'minimum_should_match': 1
            }
          };
          addAdditionalShouldQueries(elasticsearchType, queryterm,
            query.body.query.bool.should);

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
          _.each(filter, function(value, key) {
            var filterKeyValue = {
              'term': {}
            };
            if (elasticsearchType) {
              var subKeyMapping = keyMapping[elasticsearchType];
              key = subKeyMapping[key];
              filterKeyValue.term[key] = value;

              if (!query.body.query.bool.filter) {
                query.body.query.bool.filter = [];
              }

              query.body.query.bool.filter.push(
                filterKeyValue);
            }
          });
        }
        return ElasticSearchClient.search(query);
      }
    };
  });
