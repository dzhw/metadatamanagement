'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function(entity, DataSetSearchService, SurveySearchService,
      RelatedPublicationSearchService, PageTitleService, LanguageService,
      $state, ToolbarHeaderService) {
      var ctrl = this;
      ctrl.study = entity;
      ctrl.counts = {};
      entity.$promise.then(function() {
        PageTitleService.setPageTitle('study-management.detail.title', {
          title: ctrl.study.title[LanguageService.getCurrentInstantly()],
          studyId: ctrl.study.id
        });
        SurveySearchService
            .countBy('studyId', ctrl.study.id)
            .then(function(surveysCount) {
              ctrl.counts.surveysCount = surveysCount.count;
              if (surveysCount.count === 1) {
                SurveySearchService
                  .findByStudyId(ctrl.study.id, ['title', 'number', 'id'])
                  .then(function(survey) {
                    ctrl.survey = survey.hits.hits[0]._source;
                  });
              }
            });
        DataSetSearchService.countBy('studyId', ctrl.study.id)
           .then(function(dataSetsCount) {
              ctrl.counts.dataSetsCount = dataSetsCount.count;
              if (dataSetsCount.count === 1) {
                DataSetSearchService.findByStudyId(ctrl.study.id,
                 ['description', 'id']).then(function(dataSet) {
                    ctrl.dataSet = dataSet.hits.hits[0]._source;
                  });
              }
            });
        RelatedPublicationSearchService.countBy('studyIds', ctrl.study.id)
            .then(function(publicationsCount) {
                ctrl.counts.publicationsCount = publicationsCount.count;
                if (publicationsCount.count === 1) {
                  RelatedPublicationSearchService.findByStudyId(ctrl.study.id,
                  ['id', 'title']).then(function(relatedPublication) {
                    ctrl.relatedPublication = relatedPublication.
                    hits.hits[0]._source;
                  });
                }
              });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.study.id});
      });
    });
