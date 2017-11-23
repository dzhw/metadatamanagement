'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyEditOrCreateController',
    function(entity, PageTitleService, LanguageService,
      $state, ToolbarHeaderService, Principal, SimpleMessageToastService) {
      var ctrl = this;
      if (Principal.hasAuthority('ROLE_PUBLISHER')) {
        if (entity) {
          entity.$promise.then(function(study) {
            ctrl.study = study;
            console.log(study);
            PageTitleService.setPageTitle('study-management.detail.title', {
              title: study.title[LanguageService.getCurrentInstantly()],
              studyId: study.id
            });
            ToolbarHeaderService.updateToolbarHeader({
              'stateName': $state.current.name,
              'id': study.id,
              'studyIsPresent': true,
              'projectId': study.dataAcquisitionProjectId});
          });
        } else {
          ctrl.createMode = true;
          ctrl.study = {};
        }
      } else {
        /*SimpleMessageToastService.openSimpleMessageToast(
        'study-management.detail.not-released-toast', {id: study.id}
      );*/
      }
    });
