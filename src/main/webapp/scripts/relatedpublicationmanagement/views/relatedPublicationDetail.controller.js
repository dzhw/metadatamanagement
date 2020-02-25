'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationDetailController',
    function(entity, PageTitleService, $state, BreadcrumbService,
    SearchResultNavigatorService, Principal, $stateParams) {

      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();
      var ctrl = this;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER','ROLE_DATA_PROVIDER']);

      entity.promise.then(function(result) {
        ctrl.relatedPublication = result;
        // We need this for the tab creation
        ctrl.counts = {};
        ctrl.counts.dataSetsCount = 0;
        ctrl.counts.variablesCount = 0;
        ctrl.counts.studiesCount = 0;
        ctrl.counts.instrumentsCount = 0;
        ctrl.counts.questionsCount = 0;
        ctrl.counts.surveysCount = 0;

        PageTitleService.setPageTitle('related-publication-management.' +
        'detail.title', {
          title: ctrl.relatedPublication.title,
          publicationId: ctrl.relatedPublication.id
        });
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.relatedPublication.id});
      });
    });
