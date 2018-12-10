'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationDetailController',
    function(entity, PageTitleService, $state, ToolbarHeaderService,
    SearchResultNavigatorService, $stateParams, Principal) {
      SearchResultNavigatorService.registerCurrentSearchResult(
          $stateParams['search-result-index']);
      var ctrl = this;
      ctrl.searchResultIndex = $stateParams['search-result-index'];
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
