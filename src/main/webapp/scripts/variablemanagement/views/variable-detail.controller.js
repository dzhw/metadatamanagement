/* global html_beautify */
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function($scope, entity,
    QuestionSearchService, VariableSearchService, Principal,
    SimpleMessageToastService, PageTitleService, LanguageService,
    CleanJSObjectService, $state, ToolbarHeaderService) {
    $scope.generationCodeToggleFlag = true;
    $scope.filterDetailsCodeToggleFlag = true;
    $scope.notAllRowsVisible = true;
    $scope.counts = {};
    $scope.validResponsesOrMissingsAvailable = false;
    entity.promise.then(function(result) {
      var currenLanguage = LanguageService.getCurrentInstantly();
      var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
      PageTitleService.setPageTitle('variable-management.detail.title', {
        label: result.label[currenLanguage] ? result.label[currenLanguage]
          : result.label[secondLanguage],
        variableId: result.id
      });
      ToolbarHeaderService.updateToolbarHeader({
        'stateName': $state.current.name,
        'id': result.id,
        'name': result.name,
        'dataSetId': result.dataSetId,
        'dataSetNumber': result.dataSetNumber,
        'dataSetIsPresent': CleanJSObjectService.
        isNullOrEmpty(result.dataSet) ? false : true,
        'surveys': result.surveys,
        'studyId': result.studyId,
        'studyIsPresent': CleanJSObjectService.
        isNullOrEmpty(result.study) ? false : true,
        'projectId': result.dataAcquisitionProjectId});
      if (result.release || Principal.hasAuthority('ROLE_PUBLISHER')) {
        $scope.variable = result;
        if (!CleanJSObjectService.isNullOrEmpty($scope
          .variable.distribution.missings) || !CleanJSObjectService
          .isNullOrEmpty($scope.variable
            .distribution.validResponses)) {
          $scope.validResponsesOrMissingsAvailable = true;
        }
        $scope.study = $scope.variable.study;
        $scope.dataSet = $scope.variable.dataSet;
        QuestionSearchService.countBy('variables.id', $scope.variable.id)
        .then(function(questionsCount) {
          $scope.counts.questionsCount = questionsCount.count;
          if (questionsCount.count === 1) {
            QuestionSearchService
            .findByVariableId($scope.variable.id, ['number', 'instrumentNumber',
            'questionText', 'id'])
            .then(function(question) {
              $scope.question = question.hits.hits[0]._source;
            });
          }
        });
        $scope.counts.surveysCount = $scope.variable.surveys.length;
        if ($scope.counts.surveysCount === 1) {
          $scope.survey = $scope.variable.surveys[0];
        }
        if ($scope.variable.panelIdentifier) {
          VariableSearchService
          .countBy('panelIdentifier', $scope.variable.panelIdentifier)
          .then(function(variablesInPanel) {
            $scope.counts.variablesInPanel = variablesInPanel.count;
          });
        } else {
          $scope.counts.variablesInPanel = 0;
        }
        $scope.counts.publicationsCount = $scope.variable
        .relatedPublications.length;
        if ($scope.counts.publicationsCount === 1) {
          $scope.relatedPublication = $scope.variable
          .relatedPublications[0];
        }
        if ($scope.variable.filterDetails) {
          html_beautify($scope.variable.filterDetails.expression); //jscs:ignore
        }
        if ($scope.variable.generationDetails) {
          html_beautify($scope.variable.generationDetails.rule); //jscs:ignore
        }
      } else {
        SimpleMessageToastService.openSimpleMessageToast(
          'variable-management.detail.not-released-toast', {id: result.id}
        );
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
