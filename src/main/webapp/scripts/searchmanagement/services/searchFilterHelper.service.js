/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchFilterHelperService',
  function(CleanJSObjectService) {
    var keyMapping = {
      'studies': {
        'survey': 'surveys.id',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'variables': {
        'study': 'studyId',
        'survey': 'surveys.id',
        'instrument': 'instruments.id',
        'question': 'relatedQuestions.questionId',
        'data-set': 'dataSetId',
        'access-way': 'accessWays',
        'panel-identifier': 'panelIdentifier',
        'related-publication': 'relatedPublications.id'
      },
      'surveys': {
        'study': 'studyId',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'questions': {
        'study': 'studyId',
        'survey': 'surveys.id',
        'instrument': 'instrumentId',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'instruments': {
        'study': 'studyId',
        'survey': 'surveyIds',
        'question': 'questions.id',
        'data-set': 'dataSets.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'data_sets': {
        'study': 'studyId',
        'survey': 'surveyIds',
        'instrument': 'instruments.id',
        'question': 'questions.id',
        'variable': 'variables.id',
        'related-publication': 'relatedPublications.id'
      },
      'related_publications': {
        'study': 'studyIds',
        'survey': 'surveyIds',
        'instrument': 'instrumentIds',
        'question': 'questionIds',
        'data-set': 'dataSetIds',
        'variable': 'variableIds'
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
            key = subKeyMapping[key];
            if (key) {
              filterKeyValue.term[key] = value;
              termFilters.push(
                filterKeyValue);
            }
          }
        });
        return termFilters;
      }
    };

    var removeIrrelevantFilters = function(elasticsearchType, filter) {
      if (elasticsearchType) {
        var validFilterKeys = _.keys(keyMapping[elasticsearchType]);
        return _.pick(filter, validFilterKeys);
      }
      return {};
    };

    var getAvailableFilters = function(elasticsearchType) {
      return _.keys(keyMapping[elasticsearchType]);
    };

    return {
      createTermFilters: createTermFilters,
      removeIrrelevantFilters: removeIrrelevantFilters,
      getAvailableFilters: getAvailableFilters
    };
  }
);
