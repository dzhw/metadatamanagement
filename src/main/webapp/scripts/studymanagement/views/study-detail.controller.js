'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function($scope, entity, DataSetSearchService, DataSetSearchDialogService,
      SurveySearchService, SurveySearchDialogService,
      RelatedPublicationSearchDialogService,
      RelatedPublicationSearchService, PageTitleService, LanguageService) {

      var ctrl = this;
      ctrl.study = entity;
      ctrl.counts = {};

      entity.$promise.then(function() {
        PageTitleService.setPageTitle('study-management.detail.title', {
          title: ctrl.study.title[LanguageService.getCurrentInstantly()]
        });
        DataSetSearchService
          .countBy('dataAcquisitionProjectId', ctrl.study.id)
          .then(function(dataSetsCount) {
            ctrl.counts.dataSetsCount = dataSetsCount.count;
          });
        SurveySearchService
          .countBy('dataAcquisitionProjectId', ctrl.study.id)
          .then(function(surveysCount) {
            ctrl.counts.surveysCount = surveysCount.count;
          });
        RelatedPublicationSearchService
          .countBy('studyIds', ctrl.study.id)
          .then(function(publicationsCount) {
            ctrl.counts.publicationsCount = publicationsCount.count;
          });
      });
      ctrl.showRelatedDataSets = function() {
        var paramObject = {};
        paramObject.methodName = 'findByProjectId';
        paramObject.methodParams = ctrl.study.id;
        DataSetSearchDialogService.findDataSets(paramObject);
      };
      ctrl.showRelatedSurveys = function() {
        var paramObject = {};
        paramObject.methodName = 'findByProjectId';
        paramObject.methodParams = ctrl.study.id;
        SurveySearchDialogService.findSurveys(paramObject);
      };
      ctrl.showRelatedPublications = function() {
        var paramObject = {};
        paramObject.methodName = 'findByStudyId';
        paramObject.methodParams = ctrl.study.id;
        RelatedPublicationSearchDialogService.
        findRelatedPublications(paramObject);
      };
    });
