/* global _*/
'use strict';

angular.module('metadatamanagementApp').service('SearchDao',
  function(ElasticSearchClient, CleanJSObjectService, Principal) {
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

    var addAdditionalShouldQuery = function(elasticsearchType, queryterm) {
      switch (elasticsearchType) {
        case 'studies':
          return [];

        case 'surveys':
          return [{
            'match': {
              'title.de': queryterm
            }
          }, {
            'match': {
              'title.en': queryterm
            }
          }, {
            'match': {
              'id': queryterm
            }
          }, {
            'match': {
              'surveyMethod.de': queryterm
            }
          }, {
            'match': {
              'surveyMethod.en': queryterm
            }
          }, {
            'match': {
              'population.de': queryterm
            }
          }, {
            'match': {
              'population.en': queryterm
            }
          }, {
            'match': {
              'sample.de': queryterm
            }
          }, {
            'match': {
              'sample.en': queryterm
            }
          }];

        case 'instruments':
          return [];

        case 'questions':
          return [];

        case 'data_sets':
          return [];

        case 'variables':
          return [];

        case 'related_publications':
          return [];
      }
    };

    return {
      search: function(queryterm, pageNumber, dataAcquisitionProjectId,
        filter, elasticsearchType, pageSize, sortBy) {
        var query = {};
        var projectFilter;
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
          'description', 'type', 'doi', 'publicationAbstract', 'authors',
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
              'must': [{
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
              'should': addAdditionalShouldQuery(elasticsearchType,
                queryterm),
              'minimum_should_match': 1
            }
          };

          //no query term
        } else {
          query.body.query = {
            'bool': {
              'must': [{
                'match_all': {}
              }],
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
        if (dataAcquisitionProjectId ||
          !CleanJSObjectService.isNullOrEmpty(filter) ||
          !Principal.hasAuthority('ROLE_PUBLISHER')) {
          query.body.query.bool.filter = [];
        }
        //only publisher see unreleased projects
        if (!Principal.hasAuthority('ROLE_PUBLISHER')) {
          query.body.query.bool.filter.push({
            'exists': {
              'field': 'release'
            }
          });
        }
        if (dataAcquisitionProjectId) {
          projectFilter = {
            'term': {
              'dataAcquisitionProjectId': dataAcquisitionProjectId
            }
          };
          query.body.query.bool.filter.push(projectFilter);
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
              query.body.query.bool.filter.push(
                filterKeyValue);
            }
          });
        }
        return ElasticSearchClient.search(query);
      }
    };
  });
