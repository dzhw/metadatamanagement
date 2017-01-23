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
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.counts = {};
      ctrl.dataSet = entity;
      ctrl.counts = {};
      entity.$promise.then(function() {
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('data-set-management.detail.title', {
          description: ctrl.dataSet.description[currenLanguage] ? ctrl.dataSet
            .description[currenLanguage] : ctrl
            .dataSet.description[secondLanguage],
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
        //ctrl.counts.variablesCount = ctrl.dataSet.variableIds.length;
        ctrl.counts.variablesCount = 0; //TODO DKatzberg: Just a workaround
        //until issue 877 is implemented
      });
      ctrl.uploadTexTemplate = function(files) {
        if (files != null) {
          DataSetReportService.uploadTexTemplate(files, ctrl.dataSet.id);
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
