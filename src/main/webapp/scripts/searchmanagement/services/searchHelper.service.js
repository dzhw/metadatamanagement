/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchHelperService',
  function(CleanJSObjectService, Principal) {
    var keyMapping = {
      'studies': {
        'study-series-de': 'studySeries.de',
        'study-series-en': 'studySeries.en',
        'survey': 'surveys.id',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id',
        'institution-de': 'institution.de',
        'institution-en': 'institution.en',
        'sponsor-de': 'sponsor.de',
        'sponsor-en': 'sponsor.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en'
      },
      'variables': {
        'study-series-de': 'study.studySeries.de',
        'study-series-en': 'study.studySeries.en',
        'study': 'studyId',
        'survey': 'surveys.id',
        'instrument': 'instruments.id',
        'question': 'relatedQuestions.questionId',
        'data-set': 'dataSetId',
        'access-way': 'accessWays',
        'panel-identifier': 'panelIdentifier',
        'derived-variables-identifier': 'derivedVariablesIdentifier',
        'related-publication': 'relatedPublications.id',
        'institution-de': 'study.institution.de',
        'institution-en': 'study.institution.en',
        'sponsor-de': 'study.sponsor.de',
        'sponsor-en': 'study.sponsor.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en'
      },
      'surveys': {
        'study-series-de': 'study.studySeries.de',
        'study-series-en': 'study.studySeries.en',
        'study': 'studyId',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id',
        'institution-de': 'study.institution.de',
        'institution-en': 'study.institution.en',
        'sponsor-de': 'study.sponsor.de',
        'sponsor-en': 'study.sponsor.en',
        'survey-method-de': 'surveyMethod.de',
        'survey-method-en': 'surveyMethod.en'
      },
      'questions': {
        'study-series-de': 'study.studySeries.de',
        'study-series-en': 'study.studySeries.en',
        'study': 'studyId',
        'survey': 'surveys.id',
        'instrument': 'instrumentId',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id',
        'institution-de': 'study.institution.de',
        'institution-en': 'study.institution.en',
        'sponsor-de': 'study.sponsor.de',
        'sponsor-en': 'study.sponsor.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en'
      },
      'instruments': {
        'study-series-de': 'study.studySeries.de',
        'study-series-en': 'study.studySeries.en',
        'study': 'studyId',
        'survey': 'surveyIds',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id',
        'institution-de': 'study.institution.de',
        'institution-en': 'study.institution.en',
        'sponsor-de': 'study.sponsor.de',
        'sponsor-en': 'study.sponsor.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en'
      },
      'data_sets': {
        'study-series-de': 'study.studySeries.de',
        'study-series-en': 'study.studySeries.en',
        'study': 'studyId',
        'survey': 'surveyIds',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id',
        'access-way': 'accessWays',
        'institution-de': 'study.institution.de',
        'institution-en': 'study.institution.en',
        'sponsor-de': 'study.sponsor.de',
        'sponsor-en': 'study.sponsor.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en'
      },
      'related_publications': {
        'study-series-de': 'studySerieses.de',
        'study-series-en': 'studySerieses.en',
        'study': 'studyIds',
        'survey': 'surveyIds',
        'instrument': 'instrumentIds',
        'question': 'questionIds',
        'data-set': 'dataSetIds',
        'variable': 'variableIds'
      }
    };

    var hiddenFiltersKeyMapping = {
      'studies': {
        'study': '_id'
      },
      'variables': {
        'variable': '_id'
      },
      'surveys': {
        'survey': '_id'
      },
      'questions': {
        'question': '_id'
      },
      'instruments': {
        'instrument': '_id'
      },
      'data_sets': {
        'data-set': '_id'
      },
      'related_publications': {
        'related-publication': '_id'
      }
    };

    var sortCriteriaByType = {
      'studies': [
        '_score',
        'id'
      ],
      'variables': [
        '_score',
        'studyId',
        'dataSetId',
        'indexInDataSet'
      ],
      'surveys': [
        '_score',
        'studyId',
        'number'
      ],
      'questions': [
        '_score',
        'studyId',
        'instrumentNumber',
        'indexInInstrument'
      ],
      'instruments': [
        '_score',
        'studyId',
        'number'
      ],
      'data_sets': [
        '_score',
        'studyId',
        'number'
      ],
      'related_publications': [
        '_score',
        {year: {order: 'desc'}},
        'authors.keyword'
      ]
    };

    //Returns the search criteria
    var createSortByCriteria = function(elasticsearchType) {
      // no special sorting for all tab
      if (CleanJSObjectService.isNullOrEmpty(elasticsearchType)) {
        return [
          '_score'
        ];
      }

      return sortCriteriaByType[elasticsearchType];
    };

    var createTermFilters = function(elasticsearchType, filter) {
      var termFilters = [];
      if (!CleanJSObjectService.isNullOrEmpty(filter)) {
        _.each(filter, function(value, key) {
          var filterKeyValue = {
            'term': {}
          };
          if (elasticsearchType) {
            var subKeyMapping = keyMapping[elasticsearchType];
            key = subKeyMapping[key] ||
              hiddenFiltersKeyMapping[elasticsearchType][key];
            if (key) {
              filterKeyValue.term[key] = value;
              termFilters.push(filterKeyValue);
            }
          }
        });
        return termFilters;
      }
    };

    var removeIrrelevantFilters = function(elasticsearchType, filter) {
      if (elasticsearchType) {
        var validFilterKeys = _.keys(keyMapping[elasticsearchType]);
        var validHiddenFilterKeys = _.keys(
          hiddenFiltersKeyMapping[elasticsearchType]);
        return _.pick(filter, validFilterKeys, validHiddenFilterKeys);
      }
      return {};
    };

    var getAvailableFilters = function(elasticsearchType) {
      return _.keys(keyMapping[elasticsearchType]);
    };

    var getHiddenFilters = function(elasticsearchType) {
      return _.keys(hiddenFiltersKeyMapping[elasticsearchType]);
    };

    var applyReleaseFilter = function(query) {
      //only publisher and data provider see unreleased projects
      if (!Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER',
          'ROLE_ADMIN'])) {
        query.body.query = query.body.query || {};
        query.body.query.bool = query.body.query.bool || {};
        query.body.query.bool.filter = query.body.query.bool.filter || [];

        var releaseExistsFilter = {
          'exists': {
            'field': 'release'
          }
        };
        if (angular.isArray(query.body.query.bool.filter)) {
          query.body.query.bool.filter.push(releaseExistsFilter);
        } else if (angular.isDefined(query.body.query.bool.filter)) {
          var filterObj = query.body.query.bool.filter;
          query.body.query.bool.filter = [filterObj, releaseExistsFilter];
        } else {
          query.body.query.bool.filter = [releaseExistsFilter];
        }
      }
    };

    var applyDataProviderFilter = function(query) {
      //we must hide some filter options if user is only data provider
      if (Principal.hasAuthority('ROLE_DATA_PROVIDER') &&
        !Principal.hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN'])) {
        var loginName = Principal.loginName();
        if (loginName) {
          var filterCriteria = {
            'bool': {
              'must': [{
                'term': {'configuration.dataProviders': loginName}
              }]
            }
          };

          var filterArray = _.get(query, 'body.query.bool.filter');

          if (_.isArray(filterArray)) {
            filterArray.push(filterCriteria);
          } else {
            _.set(query, 'body.query.bool.filter', filterCriteria);
          }
        }
      }
    };

    var pushToFilterArray = function(query, filterEntry) {
      var filter = _.get(query, 'body.query.bool.filter');
      if (_.isArray(filter)) {
        filter.push(filterEntry);
      } else if (_.isEmpty(filter)) {
        _.set(query, 'body.query.bool.filter[0]', filterEntry);
      } else {
        _.set(query, 'body.query.bool.filter[0]', filter);
        _.set(query, 'body.query.bool.filter[1]', filterEntry);
      }
    };

    var applyOnlyMasterDataFilter = function(query) {
      var masterFilter = {
        'bool': {
          'must': [{
            'term': {'shadow': false}
          }]
        }
      };
      pushToFilterArray(query, masterFilter);
    };

    var applyShadowCopyFilter = function(query, filterLatestShadowCopy) {
      var shadowCopyFilter = {
        'bool': {
          'must': [{
            'term': {'shadow': true}
          }]
        }
      };

      if (filterLatestShadowCopy) {
        _.set(shadowCopyFilter, 'bool.must_not[0].exists.field',
          'successorId');
      }
      pushToFilterArray(query, shadowCopyFilter);
    };

    var addShadowCopyFilter = function(query, filterLatestShadowCopy) {
      if (Principal.loginName()) {
        applyOnlyMasterDataFilter(query);
      } else {
        applyShadowCopyFilter(query, filterLatestShadowCopy);
      }
    };

    var addFilter = function(query) {
      applyReleaseFilter(query);
      applyDataProviderFilter(query);
    };

    var addQuery = function(query, queryterm) {
      if (!CleanJSObjectService.isNullOrEmpty(queryterm)) {
        query.body.query = query.body.query || {};
        query.body.query.bool = query.body.query.bool || {};
        query.body.query.bool.must = query.body.query.bool.must || [];
        query.body.query.bool.must.push({
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
        });
      }
    };

    var createShadowByIdAndVersionQuery = function(id, version) {
      var query = {
        'body': {
          'query': {
            'constant_score': {
              'filter':
                {
                  'bool': {
                    'must': [
                      {
                        'term': {
                          'masterId': id
                        }
                      },
                      {
                        'term': {
                          'shadow': true
                        }
                      }
                    ]
                  }
                }
            }
          }
        }
      };

      if (version) {
        query.body.query.constant_score.filter.bool.must.push({
          'term': {
            'release.version': version
          }
        });
      } else {
        _.set(query, 'body.query.constant_score.filter.bool.must_not.exists' +
          '.field', 'successorId');
      }

      return query;
    };

    return {
      createTermFilters: createTermFilters,
      removeIrrelevantFilters: removeIrrelevantFilters,
      getAvailableFilters: getAvailableFilters,
      getHiddenFilters: getHiddenFilters,
      createSortByCriteria: createSortByCriteria,
      createShadowByIdAndVersionQuery: createShadowByIdAndVersionQuery,
      addFilter: addFilter,
      addShadowCopyFilter: addShadowCopyFilter,
      addQuery: addQuery
    };
  }
);
