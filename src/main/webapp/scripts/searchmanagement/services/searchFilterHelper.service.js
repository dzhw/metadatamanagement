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
        'study': 'studyId',
        'data-set': 'dataSetId',
        'question': 'relatedQuestions.questionId',
        'related-publication': 'relatedPublications.id',
        'panel-identifier': 'panelIdentifier',
        'access-way': 'accessWays'
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
        'study': 'studyId',
        'survey': 'surveyIds',
        'related-publication': 'relatedPublications.id'
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
