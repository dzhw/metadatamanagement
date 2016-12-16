'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetDetailController',
    function(entity, Principal, StudySearchService,
      SurveySearchDialogService, VariableSearchDialogService,
      RelatedPublicationSearchService,
      RelatedPublicationSearchDialogService, DataSetSearchDialogService,
      DataSetSearchService, DataSetReportService, PageTitleService,
      LanguageService) {
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.counts = {};
      ctrl.dataSet = entity;
      ctrl.counts = {};
      entity.$promise.then(function() {
        PageTitleService.setPageTitle('data-set-management.detail.title', {
          description: ctrl.dataSet.description[
            LanguageService.getCurrentInstantly()],
          dataSetId: ctrl.dataSet.id
        });
        StudySearchService.findStudy(ctrl.dataSet.dataAcquisitionProjectId)
          .then(function(study) {
            if (study.hits.hits.length > 0) {
              ctrl.study = study.hits.hits[0]._source;
            }
          });
        RelatedPublicationSearchService.countBy('dataSetIds',
          ctrl.dataSet.id).then(function(publicationsCount) {
          ctrl.counts.publicationsCount = publicationsCount.count;
        });
        DataSetSearchService
          .countBy('dataAcquisitionProjectId',
            ctrl.dataSet.dataAcquisitionProjectId, ctrl.dataSet.id)
          .then(function(dataSetsCount) {
            ctrl.counts.dataSetsCount = dataSetsCount.count;
          });
        ctrl.counts.surveysCount = ctrl.dataSet.surveyIds.length;
        ctrl.counts.variablesCount = ctrl.dataSet.variableIds.length;
      });
      ctrl.uploadTexTemplate = function(file, dataSetId) {
        if (file != null) {
          DataSetReportService.uploadTexTemplate(file, dataSetId);
        }
      };
      ctrl.showRelatedSurveys = function() {
        var paramObject = {};
        paramObject.methodName = 'findSurveys';
        paramObject.methodParams = ctrl.dataSet.surveyIds;
        SurveySearchDialogService.findSurveys(paramObject);
      };
      ctrl.showRelatedVariables = function() {
        var paramObject = {};
        paramObject.methodName = 'findVariables';
        paramObject.methodParams = ctrl.dataSet.variableIds;
        VariableSearchDialogService.findVariables(paramObject);
      };
      ctrl.showRelatedDataSets = function() {
        var paramObject = {};
        paramObject.methodName = 'findByProjectId';
        paramObject.methodParams = ctrl.dataSet.dataAcquisitionProjectId;
        paramObject.dataSetId = ctrl.dataSet.id;
        DataSetSearchDialogService.findDataSets(paramObject);
      };
      ctrl.showRelatedPublications = function() {
        var paramObject = {};
        paramObject.methodName = 'findByDataSetId';
        paramObject.methodParams = ctrl.dataSet.id;
        RelatedPublicationSearchDialogService.
        findRelatedPublications(paramObject);
      };
    });
