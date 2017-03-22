'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function(entity, PageTitleService, LanguageService,
      $state, ToolbarHeaderService, Principal, SimpleMessageToastService) {
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
        } else {
          SimpleMessageToastService.openSimpleMessageToast(
          'study-management.detail.not-released-toast', {id: result.id}
          );
        }
      });
    });
