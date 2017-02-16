'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetDetailController',
    function(entity, Principal, StudySearchService, SurveySearchService,
      VariableSearchService, RelatedPublicationSearchService,
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
        StudySearchService.findOneByProjectId(ctrl.dataSet.
          dataAcquisitionProjectId, ['dataAcquisitionProjectId','title', 'id'])
          .then(function(study) {
            if (study.hits.hits.length > 0) {
              ctrl.study = study.hits.hits[0]._source;
            }
          });
        SurveySearchService.countBy('dataSets.id', ctrl.dataSet.id)
          .then(function(surveysCount) {
            ctrl.counts.surveysCount = surveysCount.count;
            if (surveysCount.count === 1) {
              SurveySearchService
                .findByDataSetId(ctrl.dataSet.id, ['dataAcquisitionProjectId',
                'number', 'title'])
                .then(function(survey) {
                  ctrl.survey = survey.hits.hits[0]._source;
                });
            }
          });
        VariableSearchService.countBy('dataSetId', ctrl.dataSet.id)
        .then(function(variablesCount) {
          ctrl.counts.variablesCount = variablesCount.count;
          if (variablesCount.count === 1) {
            VariableSearchService
              .findByDataSetId(ctrl.dataSet.id, ['dataAcquisitionProjectId',
              'dataSetNumber', 'name', 'label'])
              .then(function(variable) {
                ctrl.variable = variable.hits.hits[0]._source;
              });
          }
        });
        RelatedPublicationSearchService.countBy('dataSetIds',
          ctrl.dataSet.id).then(function(publicationsCount) {
          ctrl.counts.publicationsCount = publicationsCount.count;
          if (publicationsCount.count === 1) {
            RelatedPublicationSearchService
              .findByDataSetId(ctrl.dataSet.id, ['id', 'title'])
              .then(function(relatedPublication) {
                ctrl.relatedPublication = relatedPublication.
                hits.hits[0]._source;
              });
          }
        });
        DataSetSearchService
          .countBy('dataAcquisitionProjectId',
            ctrl.dataSet.dataAcquisitionProjectId)
          .then(function(dataSetsCount) {
            ctrl.counts.dataSetsCount = dataSetsCount.count;
          });
      });
      ctrl.uploadTexTemplate = function(files) {
        if (files != null) {
          DataSetReportService.uploadTexTemplate(files, ctrl.dataSet.id);
        }
      };
    });
