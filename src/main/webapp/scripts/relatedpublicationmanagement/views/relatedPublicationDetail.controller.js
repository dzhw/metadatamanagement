'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationDetailController',
    function(entity, PageTitleService, $state, ToolbarHeaderService) {
      var ctrl = this;
      entity.promise.then(function(result) {
        ctrl.relatedPublication = result;
        PageTitleService.setPageTitle('related-publication-management.' +
        'detail.title', {
          title: ctrl.relatedPublication.title,
          publicationId: ctrl.relatedPublication.id
        });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.relatedPublication.id});
      });
    });
