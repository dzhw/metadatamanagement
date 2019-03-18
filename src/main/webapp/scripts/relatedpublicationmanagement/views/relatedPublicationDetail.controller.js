'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationDetailController',
    function(entity, PageTitleService, $state, ToolbarHeaderService,
    SearchResultNavigatorService, Principal) {

      SearchResultNavigatorService.registerCurrentSearchResult(
          SearchResultNavigatorService.getSearchIndex());
      var ctrl = this;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER','ROLE_DATA_PROVIDER']);
      ctrl.jsonExcludes = [
        'nestedStudies',
        'nestedQuestions',
        'nestedInstruments',
        'nestedSurveys',
        'nestedDataSets',
        'nestedVariables'
      ];
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
