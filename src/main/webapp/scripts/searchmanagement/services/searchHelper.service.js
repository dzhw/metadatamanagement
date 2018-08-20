/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchHelperService',
  function(CleanJSObjectService) {
    var keyMapping = {
      'studies': {
        'study-series-de': 'studySeries.de',
        'study-series-en': 'studySeries.en',
        'survey': 'surveys.id',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
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
        'related-publication': 'relatedPublications.id'
      },
      'surveys': {
        'study-series-de': 'study.studySeries.de',
        'study-series-en': 'study.studySeries.en',
        'study': 'studyId',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'questions': {
        'study-series-de': 'study.studySeries.de',
        'study-series-en': 'study.studySeries.en',
        'study': 'studyId',
        'survey': 'surveys.id',
        'instrument': 'instrumentId',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'instruments': {
        'study-series-de': 'study.studySeries.de',
        'study-series-en': 'study.studySeries.en',
        'study': 'studyId',
        'survey': 'surveyIds',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'data_sets': {
        'study-series-de': 'study.studySeries.de',
        'study-series-en': 'study.studySeries.en',
        'study': 'studyId',
        'survey': 'surveyIds',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
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

    return {
      createTermFilters: createTermFilters,
      removeIrrelevantFilters: removeIrrelevantFilters,
      getAvailableFilters: getAvailableFilters,
      getHiddenFilters: getHiddenFilters,
      createSortByCriteria: createSortByCriteria
    };
  }
);
