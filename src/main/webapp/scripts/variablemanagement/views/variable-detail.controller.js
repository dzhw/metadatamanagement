/* global _ */
/* global html_beautify */
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function($scope, entity, $state,
    SurveySearchDialogService, DataSetSearchDialogService,
    RelatedPublicationSearchDialogService, QuestionSearchDialogService,
    DataSetSearchService, QuestionSearchService, VariableSearchDialogService,
    RelatedPublicationSearchService, StudySearchService,
    SimpleMessageToastService, PageTitleService, LanguageService) {

    $scope.generationCodeToggleFlag = true;
    $scope.filterDetailsCodeToggleFlag = true;
    $scope.notAllRowsVisible = true;
    $scope.counts = {};
    $scope.variable = entity;
    entity.$promise.then(function() {
      PageTitleService.setPageTitle(
        $scope.variable.label[LanguageService.getCurrentInstantly()] +
        ' (' + $scope.variable.name + ')');
      StudySearchService
        .findStudy($scope.variable.dataAcquisitionProjectId)
        .then(function(study) {
          if (study.hits.hits.length > 0) {
            $scope.study = study.hits.hits[0]._source;
          }
        });
      if ($scope.variable.questionId) {
        var questionIdAsArray = [];
        questionIdAsArray.push($scope.variable.questionId);
        QuestionSearchService
          .findQuestions(questionIdAsArray)
          .then(function(question) {
            _.pullAllBy(question.docs, [{
                'found': false
              }],
              'found');
            if (question.docs.length > 0) {
              $scope.question = question.docs[0]._source;
            }
          });
      }
      DataSetSearchService
        .countBy('variableIds', $scope.variable.id)
        .then(function(dataSetsCount) {
          $scope.counts.dataSetsCount = dataSetsCount.count;
        });
      RelatedPublicationSearchService
        .countBy('variableIds', $scope.variable.id)
        .then(function(publicationsCount) {
          $scope.counts.publicationsCount = publicationsCount.count;
        });
      $scope.counts.surveysCount = $scope.variable.surveyIds.length;

      if ($scope.variable.sameVariablesInPanel != null) {
        $scope.counts.sameVariablesInPanel =
          $scope.variable.sameVariablesInPanel.length;
      }
      if ($scope.variable.filterDetails) {
        html_beautify($scope.variable.filterDetails.expression); //jscs:ignore
      }
      if ($scope.variable.generationDetails) {
        html_beautify($scope.variable.generationDetails.rule); //jscs:ignore
      }
    });
    $scope.isRowHidden = function(index) {
      if (index <= 4 || index >= $scope
        .variable.distribution.validResponses.length - 5) {
        return false;
      } else {
        return $scope.notAllRowsVisible;
      }
    };
    $scope.toggleAllRowsVisible = function() {
      $scope.notAllRowsVisible = !$scope.notAllRowsVisible;
    };
    $scope.toggleGenerationCode = function() {
      $scope.generationCodeToggleFlag = !$scope.generationCodeToggleFlag;
    };
    $scope.toggleFilterDetailsCode = function() {
      $scope.filterDetailsCodeToggleFlag = !$scope.filterDetailsCodeToggleFlag;
    };
    $scope.showSameVariablesInPanel = function() {
      var paramObject = {};
      paramObject.methodName = 'findVariables';
      paramObject.methodParams = $scope.variable.sameVariablesInPanel;
      VariableSearchDialogService.findVariables(paramObject);
    };
    $scope.showRelatedSurveys = function() {
      var paramObject = {};
      paramObject.methodName = 'findSurveys';
      paramObject.methodParams = $scope.variable.surveyIds;
      SurveySearchDialogService.findSurveys(paramObject);
    };
    $scope.showRelatedDataSets = function() {
      var paramObject = {};
      paramObject.methodName = 'findByVariableId';
      paramObject.methodParams = $scope.variable.id;
      DataSetSearchDialogService.findDataSets(paramObject);
    };
    $scope.showRelatedPublications = function() {
      var paramObject = {};
      paramObject.methodName = 'findByVariableId';
      paramObject.methodParams = $scope.variable.id;
      RelatedPublicationSearchDialogService
        .findRelatedPublications(paramObject);
    };
    $scope.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []);
    };

    /* Show headline for Central Tendency,
      if one element is filled with data. */
    $scope.checkCentralTendencyElements = function() {

      return $scope.variable.distribution != null &&
        $scope.variable.distribution.statistics != null &&
        ($scope.variable.distribution.statistics.meanValue != null ||
          $scope.variable.distribution.statistics.median != null ||
          $scope.variable.distribution.statistics.mode != null);
    };

    /* Show headline for Dispersion, if one element is filled with data. */
    $scope.checkDispersionElements = function() {
      return $scope.variable.distribution != null &&
        $scope.variable.distribution.statistics != null &&
        ($scope.variable.distribution.statistics.standardDeviation != null ||
          $scope.variable.distribution.statistics.meanDeviation != null ||
          $scope.variable.distribution.statistics.deviance != null);
    };

    /* Show headline for Distribution, if one element is filled with data. */
    $scope.checkDistributionElements = function() {
      return $scope.variable.distribution != null &&
        $scope.variable.distribution.statistics != null &&
        ($scope.variable.distribution.statistics.skewness != null ||
          $scope.variable.distribution.statistics.kurtosis != null);
    };

  });
