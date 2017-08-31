'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function(entity, PageTitleService, LanguageService, DataSetSearchService,
      $state, ToolbarHeaderService, Principal, SimpleMessageToastService,
      StudyAttachmentResource) {
      var ctrl = this;
      ctrl.counts = {};
      entity.promise.then(function(result) {
        PageTitleService.setPageTitle('study-management.detail.title', {
          title: result.title[LanguageService.getCurrentInstantly()],
          studyId: result.id
        });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'studyIsPresent': true,
          'projectId': result.dataAcquisitionProjectId});
        if (result.release || Principal.hasAuthority('ROLE_PUBLISHER')) {
          ctrl.study = result;
          ctrl.counts.surveysCount = result.surveys.length;
          if (ctrl.counts.surveysCount === 1) {
            ctrl.survey = result.surveys[0];
          }
          ctrl.counts.dataSetsCount = result.dataSets.length;
          if (ctrl.counts.dataSetsCount === 1) {
            ctrl.dataSet = result.dataSets[0];
          }
          ctrl.counts.publicationsCount = result.relatedPublications.length;
          if (ctrl.counts.publicationsCount === 1) {
            ctrl.relatedPublication = result.relatedPublications[0];
          }
          ctrl.counts.variablesCount = result.variables.length;
          if (ctrl.counts.variablesCount === 1) {
            ctrl.variable = result.variables[0];
          }
          ctrl.counts.questionsCount = result.questions.length;
          if (ctrl.counts.questionsCount === 1) {
            ctrl.question = result.questions[0];
          }
          ctrl.counts.instrumentsCount = result.instruments.length;
          if (ctrl.counts.instrumentsCount === 1) {
            ctrl.instrument = result.instruments[0];
          }
          /* We should discuss if we need to extand the dataSet sub document
          to contain type, subDataSets and surveys.
          we need this properties only at this place*/
          DataSetSearchService.findByStudyId(result.id,
            ['id', 'number', 'description', 'type', 'subDataSets', 'surveys'])
            .then(function(dataSets) {
              ctrl.dataSets = dataSets.hits.hits;
            });
          StudyAttachmentResource.findByStudyId({
              id: ctrl.study.id
            }).$promise.then(
              function(attachments) {
                if (attachments.length > 0) {
                  ctrl.attachments = attachments;
                }
              });
        } else {
          SimpleMessageToastService.openSimpleMessageToast(
          'study-management.detail.not-released-toast', {id: result.id}
          );
        }
      });
    });
