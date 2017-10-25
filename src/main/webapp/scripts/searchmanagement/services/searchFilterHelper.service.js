/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchFilterHelperService',
  function(CleanJSObjectService) {
    var keyMapping = {
      'studies': {
        'survey-series-de': 'surveySeries.de',
        'survey-series-en': 'surveySeries.en',
        'survey': 'surveys.id',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'variables': {
        'survey-series-de': 'study.surveySeries.de',
        'survey-series-en': 'study.surveySeries.en',
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
        'survey-series-de': 'study.surveySeries.de',
        'survey-series-en': 'study.surveySeries.en',
        'study': 'studyId',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'questions': {
        'survey-series-de': 'study.surveySeries.de',
        'survey-series-en': 'study.surveySeries.en',
        'study': 'studyId',
        'survey': 'surveys.id',
        'instrument': 'instrumentId',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'instruments': {
        'survey-series-de': 'study.surveySeries.de',
        'survey-series-en': 'study.surveySeries.en',
        'study': 'studyId',
        'survey': 'surveyIds',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'data_sets': {
        'survey-series-de': 'study.surveySeries.de',
        'survey-series-en': 'study.surveySeries.en',
        'study': 'studyId',
        'survey': 'surveyIds',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'related_publications': {
        'survey-series-de': 'studies.surveySeries.de',
        'survey-series-en': 'studies.surveySeries.en',
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

    //Filter: No
    //Query: No
    var sortByCriteriaWithoutFilterWithoutQuery = {
      'studies': [{
            'id': {
              'order': 'asc'
            }
          }]
    };

    //Filter: Yes
    //Query: No
    var sortByCriteriaWithFilterWithoutQuery = {
      'studies': [{
          '_id': {
            'order': 'asc'
          }
        }]
    };

    //Filter: No
    //Query: Yes
    var sortByCriteriaWithoutFilterWithQuery = {
      'studies': [
        '_score'
      ]
    };

    //Filter: Yes
    //Query: Yes
    var sortByCriteriaWithFilterWithQuery = {
      'studies': [
        '_score'
      ]
    };

    //Returns the search criteria depending on filter and queryTerm.
    var createSortByCriteria = function(elasticsearchType, filter, queryTerm) {

      //Filter: No
      //Query: No
      if (CleanJSObjectService.isNullOrEmpty(filter) &&
        CleanJSObjectService.isNullOrEmpty(queryTerm)) {
        return sortByCriteriaWithoutFilterWithoutQuery[elasticsearchType];
      }

      //Filter: Yes
      //Query: No
      if (!CleanJSObjectService.isNullOrEmpty(filter) &&
        CleanJSObjectService.isNullOrEmpty(queryTerm)) {
        return sortByCriteriaWithFilterWithoutQuery[elasticsearchType];
      }

      //Filter: No
      //Query: Yes
      if (CleanJSObjectService.isNullOrEmpty(filter) &&
        !CleanJSObjectService.isNullOrEmpty(queryTerm)) {
        return sortByCriteriaWithoutFilterWithQuery[elasticsearchType];
      }

      //Filter: Yes
      //Query: Yes
      if (!CleanJSObjectService.isNullOrEmpty(filter) &&
        !CleanJSObjectService.isNullOrEmpty(queryTerm)) {
        return sortByCriteriaWithFilterWithQuery[elasticsearchType];
      }
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
