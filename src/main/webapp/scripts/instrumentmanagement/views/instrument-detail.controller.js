'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentDetailController',
    function(entity, InstrumentAttachmentResource,
      PageTitleService, LanguageService, $state, CleanJSObjectService,
      ToolbarHeaderService, Principal, SimpleMessageToastService) {
      //Controller Init
      var ctrl = this;
      ctrl.survey = null;
      ctrl.attachments = null;
      ctrl.study = null;
      ctrl.questionCount = null;
      //Wait for instrument Promise
      entity.promise.then(function(result) {
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'number': result.number,
          'instrumentIsPresent': true,
          'surveys': result.surveys,
          'studyId': result.studyId,
          'studyIsPresent': CleanJSObjectService.
          isNullOrEmpty(result.study) ? false : true,
          'projectId': result.dataAcquisitionProjectId});
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('instrument-management.' +
        'detail.page-title', {
          description: result.description[currenLanguage] ?
          result.description[currenLanguage] :
          result.description[secondLanguage],
          instrumentId: result.id
        });
        if (result.release || Principal.hasAuthority('ROLE_PUBLISHER')) {
          ctrl.instrument = result;
          //load all related objects in parallel
          InstrumentAttachmentResource.findByInstrumentId({
            id: ctrl.instrument.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });

          ctrl.surveyCount = result.surveys.length;
          if (ctrl.surveyCount === 1) {
            ctrl.survey = result.surveys[0];
          }

          ctrl.study = result.study;

          ctrl.questionCount = result.questions.length;
          if (ctrl.questionCount === 1) {
            ctrl.question = result.questions[0];
          }

          ctrl.publicationCount = result.relatedPublications.length;
          if (ctrl.publicationCount === 1) {
            ctrl.relatedPublication = result.relatedPublications[0];
          }
        } else {
          SimpleMessageToastService.openSimpleMessageToast(
          'instrument-management.detail.not-released-toast', {id: result.id}
          );
        }
      });
    });
