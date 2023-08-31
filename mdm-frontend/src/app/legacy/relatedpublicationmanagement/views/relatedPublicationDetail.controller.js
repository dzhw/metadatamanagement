'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationDetailController', [
  'entity',
  'PageMetadataService',
  '$state',
  'BreadcrumbService',
  'SearchResultNavigatorService',
  'Principal',
  '$stateParams',
  '$mdSidenav',
    function(entity, PageMetadataService, $state, BreadcrumbService,
    SearchResultNavigatorService, Principal, $stateParams,
    $mdSidenav) {
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER','ROLE_DATA_PROVIDER']);

      entity.promise.then(function(result) {
        ctrl.relatedPublication = result;
        // We need this for the tab creation
        ctrl.counts = {};
        ctrl.counts.dataPackagesCount = 0;
        ctrl.counts.analysisPackagesCount = 0;

        PageMetadataService.setPageTitle('related-publication-management.' +
        'detail.title', {
          title: ctrl.relatedPublication.title
        });
        PageMetadataService.setPageDescription(
          'related-publication-management.detail.description', {
          sourceReference: ctrl.relatedPublication.sourceReference
        });
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.relatedPublication.id});
      });

      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };
    }]);

