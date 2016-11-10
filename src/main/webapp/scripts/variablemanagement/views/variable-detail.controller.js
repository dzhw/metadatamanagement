'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function($scope, entity, $state,
    SurveySearchDialogService, DataSetSearchDialogService,
    RelatedPublicationSearchDialogService, QuestionSearchDialogService,
    DataSetSearchResource, QuestionSearchResource,
    RelatedPublicationSearchResource, StudySearchResource,
    SimpleMessageToastService) {
    $scope.generationCodeToggleFlag = true;
    $scope.notAllRowsVisible = true;
    $scope.counts = {};
    entity.$promise.then(function(variable) {
      $scope.variable = variable;
      StudySearchResource
      .findStudy($scope.variable.dataAcquisitionProjectId)
      .then(function(study) {
        $scope.study = study.hits.hits[0]._source;
      });
      if ($scope.variable.questionId) {
        QuestionSearchResource
        .findQuestion($scope.variable.questionId)
        .then(function(question) {
          $scope.question = question.hits.hits[0]._source;
        });
      }
      DataSetSearchResource
      .getCounts('variableIds', $scope.variable.id)
      .then(function(dataSetsCount) {
        $scope.counts.dataSetsCount = dataSetsCount.count;
      });
      RelatedPublicationSearchResource
      .getCounts('variableIds', $scope.variable.id)
      .then(function(publicationsCount) {
        $scope.counts.publicationsCount = publicationsCount.count;
      });
      $scope.counts.surveysCount =  $scope.variable.surveyIds.length;
      $scope.formulas = [];
      $scope.formulas.push({
        expression: '\\min',
        name: 'variable-management.detail.statistics.firstQuartile',
        value: variable.distribution.statistics.minimum
      });
      $scope.formulas.push({
        expression: '\\Q_{.25}',
        name: 'variable-management.detail.statistics.minimum',
        value: variable.distribution.statistics.firstQuartile
      });
      $scope.formulas.push({
        expression: '\\tilde x',
        name: 'variable-management.detail.statistics.median',
        value: variable.distribution.statistics.median
      });
      $scope.formulas.push({
        expression: '\\bar X',
        name: 'variable-management.detail.statistics.meanValue',
        value: variable.distribution.statistics.meanValue
      });
      $scope.formulas.push({
        expression: '\\Q_{.75}',
        name: 'variable-management.detail.statistics.thirdQuartile',
        value: variable.distribution.statistics.thirdQuartile
      });
      $scope.formulas.push({
        expression: '\\max',
        name: 'variable-management.detail.statistics.maximum',
        value: variable.distribution.statistics.maximum
      });
      $scope.formulas.push({
        expression: '\\vartheta',
        name: 'variable-management.detail.statistics.skewness',
        value: variable.distribution.statistics.skewness
      });
      $scope.formulas.push({
        expression: '\\omega',
        name: 'variable-management.detail.statistics.kurtosis',
        value: variable.distribution.statistics.kurtosis
      });
      $scope.formulas.push({
        expression: '\\sigma',
        name: 'variable.distribution.statistics.standardDeviation',
        value: variable.distribution.statistics.standardDeviation
      });
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
    $scope.showRelatedSurveys = function() {
      SurveySearchDialogService.findSurveys('findSurveys',
      $scope.variable.surveyIds, $scope.counts.surveysCount);
    };
    $scope.showRelatedDataSets = function() {
      DataSetSearchDialogService.findDataSets('findByVariableId',
      $scope.variable.id, $scope.counts.dataSetsCount);
    };
    $scope.showRelatedPublications = function() {
      RelatedPublicationSearchDialogService.
      findRelatedPublications('findByVariableId', $scope.variable.id,
      $scope.counts.publicationsCount);
    };
    $scope.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []
      );
    };
  });
