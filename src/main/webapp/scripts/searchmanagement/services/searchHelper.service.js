/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchHelperService',
  function(CleanJSObjectService, Principal, LanguageService) {
    var domainObjectFilterNames = ['data-package', 'analysis-package','survey',
      'data-set', 'instrument', 'variable', 'question'];

    var aggregationMapping = {
      'data_packages': {
        'study-series': {
          attribute: 'studySeries',
          i18n: true,
          min_doc_count: 1
        },
        'survey-data-types': {
          attribute: 'surveyDataTypes',
          i18n: true,
          min_doc_count: 1
        },
        'access-ways': {
          attribute: 'accessWays',
          i18n: false,
          min_doc_count: 1
        },
        'tags': {
          attribute: 'tags',
          i18n: true,
          min_doc_count: 1
        },
        'concepts': {
          attribute: 'concepts.title',
          i18n: true,
          min_doc_count: 1
        },
        'sponsors': {
          attribute: 'sponsors',
          i18n: true,
          min_doc_count: 1
        },
        'institutions': {
          attribute: 'institutions',
          i18n: true,
          min_doc_count: 1
        }
      },
      'analysis_packages': {
        'sponsors': {
          attribute: 'sponsors',
          i18n: true,
          min_doc_count: 1
        },
        'institutions': {
          attribute: 'institutions',
          i18n: true,
          min_doc_count: 1
        },
        'tags': {
          attribute: 'tags',
          i18n: true,
          min_doc_count: 1
        }
      },
      'related_publications': {
        'language': {
          attribute: 'language',
          i18n: true,
          min_doc_count: 1
        },
        'year': {
          attribute: 'year',
          i18n: true,
          min_doc_count: 1,
          orderByKey: 'desc'
        }
      }
    };

    var filterMapping = {
      'data_packages': {
        'study-series': {
          attribute: 'studySeries',
          i18n: true,
          concatMultipleWithOr: true
        },
        'survey-data-types': {
          attribute: 'surveyDataTypes',
          i18n: true,
          concatMultipleWithOr: true
        },
        'access-ways': {
          attribute: 'accessWays',
          i18n: false,
          concatMultipleWithOr: true
        },
        'tags': {
          attribute: 'tags',
          i18n: true,
          concatMultipleWithOr: false
        },
        'concepts': {
          attribute: 'concepts.title',
          i18n: true,
          concatMultipleWithOr: false
        },
        'sponsors': {
          attribute: 'sponsors',
          i18n: true,
          concatMultipleWithOr: false
        },
        'institutions': {
          attribute: 'institutions',
          i18n: true,
          concatMultipleWithOr: false
        }
      },
      'analysis_packages': {
        'sponsors': {
          attribute: 'sponsors',
          i18n: true,
          concatMultipleWithOr: false
        },
        'institutions': {
          attribute: 'institutions',
          i18n: true,
          concatMultipleWithOr: false
        },
        'tags': {
          attribute: 'tags',
          i18n: true,
          min_doc_count: 1
        }
      }
    };

    var keyMapping = {
      'data_packages': {
        'study-series-de': 'studySeries.de',
        'study-series-en': 'studySeries.en',
        'survey': 'surveys.id',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id',
        'institution-de': 'institutions.de',
        'institution-en': 'institutions.en',
        'sponsor-de': 'sponsors.de',
        'sponsor-en': 'sponsors.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en',
        'concept': 'concepts.id'
      },
      'analysis_packages': {
        'related-publication': 'relatedPublications.id',
        'institution-de': 'institutions.de',
        'institution-en': 'institutions.en',
        'sponsor-de': 'sponsors.de',
        'sponsor-en': 'sponsors.en'
      },
      'variables': {
        'study-series-de': 'dataPackage.studySeries.de',
        'study-series-en': 'dataPackage.studySeries.en',
        'data-package': 'dataPackageId',
        'survey': 'surveys.id',
        'instrument': 'instruments.id',
        'question': 'relatedQuestions.questionId',
        'data-set': 'dataSetId',
        'access-way': 'accessWays',
        'repeated-measurement-identifier': 'repeatedMeasurementIdentifier',
        'derived-variables-identifier': 'derivedVariablesIdentifier',
        'institution-de': 'dataPackage.institutions.de',
        'institution-en': 'dataPackage.institutions.en',
        'sponsor-de': 'dataPackage.sponsors.de',
        'sponsor-en': 'dataPackage.sponsors.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en',
        'concept': 'concepts.id'
      },
      'surveys': {
        'study-series-de': 'dataPackage.studySeries.de',
        'study-series-en': 'dataPackage.studySeries.en',
        'data-package': 'dataPackageId',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'institution-de': 'dataPackage.institutions.de',
        'institution-en': 'dataPackage.institutions.en',
        'sponsor-de': 'dataPackage.sponsors.de',
        'sponsor-en': 'dataPackage.sponsors.en',
        'survey-method-de': 'surveyMethod.de',
        'survey-method-en': 'surveyMethod.en',
        'concept': 'concepts.id'
      },
      'questions': {
        'study-series-de': 'dataPackage.studySeries.de',
        'study-series-en': 'dataPackage.studySeries.en',
        'data-package': 'dataPackageId',
        'survey': 'surveys.id',
        'instrument': 'instrumentId',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'institution-de': 'dataPackage.institutions.de',
        'institution-en': 'dataPackage.institutions.en',
        'sponsor-de': 'dataPackage.sponsors.de',
        'sponsor-en': 'dataPackage.sponsors.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en',
        'concept': 'conceptIds'
      },
      'instruments': {
        'study-series-de': 'dataPackage.studySeries.de',
        'study-series-en': 'dataPackage.studySeries.en',
        'data-package': 'dataPackageId',
        'survey': 'surveyIds',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'institution-de': 'dataPackage.institutions.de',
        'institution-en': 'dataPackage.institutions.en',
        'sponsor-de': 'dataPackage.sponsors.de',
        'sponsor-en': 'dataPackage.sponsors.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en',
        'concept': 'concepts.id'
      },
      'data_sets': {
        'study-series-de': 'dataPackage.studySeries.de',
        'study-series-en': 'dataPackage.studySeries.en',
        'data-package': 'dataPackageId',
        'survey': 'surveyIds',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'variable': 'variables.id',
        'access-way': 'accessWays',
        'institution-de': 'dataPackage.institutions.de',
        'institution-en': 'dataPackage.institutions.en',
        'sponsor-de': 'dataPackage.sponsors.de',
        'sponsor-en': 'dataPackage.sponsors.en',
        'survey-method-de': 'surveys.surveyMethod.de',
        'survey-method-en': 'surveys.surveyMethod.en',
        'concept': 'concepts.id'
      },
      'related_publications': {
        'study-series-de': 'dataPackages.studySeries.de',
        'study-series-en': 'dataPackages.studySeries.en',
        'data-package': 'dataPackageIds',
        'analysis-package': 'analysisPackageIds',
        'language': 'language',
        'year': 'year'
      },
      'concepts': {
        'data-package': 'dataPackages.id',
        'survey': 'surveys.id',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id'
      },
    };

    var hiddenFiltersKeyMapping = {
      'data_packages': {
        'data-package': '_id'
      },
      'analysis_packages': {
        'analysis-package': '_id'
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
      },
      'concepts': {
        'concept': '_id'
      }
    };

    var sortCriteriaByCriteriaAndTypeAndLanguage = {
      'relevance': {
        'data_packages': {
          de: [
            '_score',
            {'release.pinToStartPage': {order: 'desc'}},
            {'release.lastDate': {order: 'desc'}}
          ],
          en: [
            '_score',
            {'release.pinToStartPage': {order: 'desc'}},
            {'release.lastDate': {order: 'desc'}}
          ]
        },
        'analysis_packages': {
          de: [
            '_score',
            {'release.lastDate': {order: 'desc'}}
          ],
          en: [
            '_score',
            {'release.lastDate': {order: 'desc'}}
          ]
        },
        'variables': {
          de: [
            '_score',
            'dataPackageId',
            'dataSetId',
            'indexInDataSet'
          ],
          en: [
            '_score',
            'dataPackageId',
            'dataSetId',
            'indexInDataSet'
          ]
        },
        'surveys': {
          de: [
            '_score',
            'dataPackageId',
            'serialNumber',
            'number'
          ],
          en: [
            '_score',
            'dataPackageId',
            'serialNumber',
            'number'
          ]
        },
        'questions': {
          de: [
            '_score',
            'dataPackageId',
            'instrumentNumber',
            'indexInInstrument'
          ],
          en: [
            '_score',
            'dataPackageId',
            'instrumentNumber',
            'indexInInstrument'
          ]
        },
        'instruments': {
          de: [
            '_score',
            'dataPackageId',
            'number'
          ],
          en: [
            '_score',
            'dataPackageId',
            'number'
          ]
        },
        'data_sets': {
          de: [
            '_score',
            'dataPackageId',
            'number'
          ],
          en: [
            '_score',
            'dataPackageId',
            'number'
          ]
        },
        'related_publications': {
          de: [
            '_score',
            {year: {order: 'desc'}},
            'authors.keyword'
          ],
          en: [
            '_score',
            {year: {order: 'desc'}},
            'authors.keyword'
          ]
        },
        'concepts': {
          de: [
            '_score',
            'id'
          ],
          en: [
            '_score',
            'id'
          ]
        }
      },
      'alphabetically': {
        'data_packages': {
          de: [
            'title.de'
          ],
          en: [
            'title.en'
          ]
        },
        'analysis_packages': {
          de: [
            'title.de'
          ],
          en: [
            'title.en'
          ]
        },
        'variables': {
          de: [
            'label.de'
          ],
          en: [
            'label.en'
          ]
        },
        'surveys': {
          de: [
            'title.de'
          ],
          en: [
            'title.en'
          ]
        },
        'questions': {
          de: [
            'questionText.de'
          ],
          en: [
            'questionText.en'
          ]
        },
        'instruments': {
          de: [
            'title.de'
          ],
          en: [
            'title.en'
          ]
        },
        'data_sets': {
          de: [
            'description.de'
          ],
          en: [
            'description.en'
          ]
        },
        'related_publications': {
          de: [
            'title'
          ],
          en: [
            'title'
          ]
        },
        'concepts': {
          de: [
            'title.de'
          ],
          en: [
            'title.en'
          ]
        }
      },
      'survey-period': {
        'data_packages': {
          de: [{'surveyPeriod.end': {order: 'desc'}}],
          en: [{'surveyPeriod.end': {order: 'desc'}}]
        }
      },
      'last-release-date': {
        'data_packages': {
          de: [{'release.lastDate': {order: 'desc'}}],
          en: [{'release.lastDate': {order: 'desc'}}]
        },
        'analysis_packages': {
          de: [{'release.lastDate': {order: 'desc'}}],
          en: [{'release.lastDate': {order: 'desc'}}]
        }
      },
      'first-release-date': {
        'data_packages': {
          de: [{'release.firstDate': {order: 'desc'}}],
          en: [{'release.firstDate': {order: 'desc'}}]
        },
        'analysis_packages': {
          de: [{'release.firstDate': {order: 'desc'}}],
          en: [{'release.firstDate': {order: 'desc'}}]
        }
      }
    };

    var containsDomainObjectFilter = function(filter) {
      var configuredFilterNames = _.keys(filter);
      return _.find(configuredFilterNames, function(configuredFilterName) {
        return domainObjectFilterNames.indexOf(configuredFilterName) !== -1;
      }) !== undefined;
    };

    //Returns the sort criteria
    var createSortByCriteria = function(elasticsearchType, criteria) {
      criteria = criteria || 'relevance';
      var currentLanguage = LanguageService.getCurrentInstantly();
      // no special sorting for all tab
      if (CleanJSObjectService.isNullOrEmpty(elasticsearchType)) {
        return [
          '_score'
        ];
      }

      return sortCriteriaByCriteriaAndTypeAndLanguage
        [criteria][elasticsearchType][currentLanguage];
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
            _.set(query, 'body.query.bool.filter[0]', filterCriteria);
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

    var applyOnlyMasterDataFilter = function(query, filter) {
      var masterFilter = {
        'bool': {
          'must': [{
            'term': {'shadow': false}
          }]
        }
      };
      if (!containsDomainObjectFilter(filter)) {
        pushToFilterArray(query, masterFilter);
      }
    };

    var applyShadowCopyFilter = function(query, filter) {
      var shadowCopyFilter = {
        'bool': {
          'must': [{
            'term': {'shadow': true}
          },
          {
            'term': {'hidden': false}
          }]
        }
      };

      if (!containsDomainObjectFilter(filter)) {
        _.set(shadowCopyFilter, 'bool.must_not[0].exists.field',
          'successorId');
      }
      pushToFilterArray(query, shadowCopyFilter);
    };

    var addShadowCopyFilter = function(query, filter, enforceReleased) {
      if (!enforceReleased && Principal.loginName()) {
        applyOnlyMasterDataFilter(query, filter);
      } else {
        applyShadowCopyFilter(query, filter);
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

      if (!Principal.loginName()) {
        query.body.query.constant_score.filter.bool.must.push({
          'term': {
            'hidden': false
          }
        });
      }

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

    var createMasterByIdQuery = function(id) {
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
                          'shadow': false
                        }
                      }
                    ]
                  }
                }
            }
          }
        }
      };

      if (!Principal.loginName()) {
        query.body.query.constant_score.filter.bool.must.push({
          'term': {
            'hidden': false
          }
        });
      }

      _.set(query, 'body.query.constant_score.filter.bool.must_not.exists' +
        '.field', 'successorId');

      return query;
    };

    var addNestedShadowCopyFilter = function(boolFilter, path, type) {
      var termFilter = {};
      if (type === 'concepts') {
        if (Principal.loginName()) {
          _.set(termFilter, 'term.["' + path + 'shadow"]', false);
          boolFilter.must.push(termFilter);
        } else {
          _.set(termFilter, 'term.["' + path + 'shadow"]', true);
          boolFilter.must.push(termFilter);
          _.set(boolFilter, 'must_not[0].exists.field', 'successorId');
        }
      }
    };

    var addAggregations = function(query, elasticsearchType, aggregations) {
      var currentLanguage = LanguageService.getCurrentInstantly();
      if (aggregations && aggregations.length > 0) {
        query.body.aggs = {
          all: {
            global: {},
            aggs: {
              filtered: {
                aggs: {}
              }
            }
          }
        };

        if (!Principal.loginName()) {
          var shadowCopyFilter = {
            'bool': {
              'must': [{
                'term': {'shadow': true}
              },{
                'term': {'hidden': false}
              }]
            }
          };
          _.set(shadowCopyFilter, 'bool.must_not[0].exists.field',
            'successorId');
          _.set(query.body.aggs.all.aggs.filtered, 'filter', shadowCopyFilter);
        } else {
          var masterFilter = {
            'bool': {
              'must': [{
                'term': {'shadow': false}
              }]
            }
          };
          _.set(query.body.aggs.all.aggs.filtered, 'filter', masterFilter);
        }
        aggregations.forEach(function(attribute) {
          var aggregationConfig = _.get(aggregationMapping,
            elasticsearchType + '.' + attribute);
          if (aggregationConfig) {
            var aggregation = {
              terms: {
                size: 100,
                min_doc_count: aggregationConfig.min_doc_count
              }
            };

            if (aggregationConfig.orderByKey) {
              aggregation.terms.order = {'_key': aggregationConfig.orderByKey};
            }

            if (aggregationConfig.i18n &&
              elasticsearchType !== 'related_publications') {
              aggregation.terms.field = aggregationConfig.attribute +
              '.' + currentLanguage;
            } else {
              aggregation.terms.field = aggregationConfig.attribute;
            }
            _.set(query.body.aggs, attribute, aggregation);
            _.set(query.body.aggs.all.aggs.filtered.aggs, attribute,
              aggregation);
          }
        });
      }
    };

    var addNewFilters = function(query, elasticsearchType, newFilters) {
      var currentLanguage = LanguageService.getCurrentInstantly();
      if (newFilters && _.keys(newFilters).length > 0) {
        var allFilters = {
          bool: {
            must: []
          }
        };
        _.keys(newFilters).forEach(function(filterKey) {
          var filterConfig = _.get(
            filterMapping, elasticsearchType + '.' + filterKey);
          if (!filterConfig) {
            return;
          }
          var attributeFilter;
          var filterArray;
          if (filterConfig.concatMultipleWithOr) {
            attributeFilter = {
              bool: {
                should: [],
                minimum_should_match: 1
              }
            };
            filterArray = attributeFilter.bool.should;
          } else {
            attributeFilter = {
              bool: {
                must: []
              }
            };
            filterArray = attributeFilter.bool.must;
          }

          var createFilter = function(filterValue) {
            var filter = {
              term: {
              }
            };
            if (filterConfig.i18n) {
              filter.term[filterConfig.attribute + '.' + currentLanguage] =
              filterValue;
            } else {
              filter.term[filterConfig.attribute] = filterValue;
            }
            filterArray.push(filter);
          };

          if (Array.isArray(newFilters[filterKey])) {
            newFilters[filterKey].forEach(createFilter);
          } else {
            createFilter(newFilters[filterKey]);
          }
          allFilters.bool.must.push(attributeFilter);
        });
        if (!query.body.query.bool.filter) {
          query.body.query.bool.filter = [];
        }
        query.body.query.bool.filter.push(allFilters);
      }
    };

    return {
      containsDomainObjectFilter: containsDomainObjectFilter,
      createTermFilters: createTermFilters,
      removeIrrelevantFilters: removeIrrelevantFilters,
      getAvailableFilters: getAvailableFilters,
      getHiddenFilters: getHiddenFilters,
      createSortByCriteria: createSortByCriteria,
      createShadowByIdAndVersionQuery: createShadowByIdAndVersionQuery,
      createMasterByIdQuery: createMasterByIdQuery,
      addFilter: addFilter,
      addShadowCopyFilter: addShadowCopyFilter,
      addNestedShadowCopyFilter: addNestedShadowCopyFilter,
      addQuery: addQuery,
      addAggregations: addAggregations,
      addNewFilters: addNewFilters
    };
  }
);
