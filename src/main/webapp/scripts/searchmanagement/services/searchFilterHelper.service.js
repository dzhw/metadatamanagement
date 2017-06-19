/* global _ */
'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchFilterHelperService',
  function(CleanJSObjectService) {
    var keyMapping = {
      'studies': {
        'related-publication': 'relatedPublications.id',
        'data-set': 'dataSets.id',
        'question': 'questions.id'
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
    return {
      createTermFilters: createTermFilters,
      removeIrrelevantFilters: removeIrrelevantFilters
    };
  }
);
