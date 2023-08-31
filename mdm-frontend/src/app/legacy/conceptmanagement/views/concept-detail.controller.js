'use strict';

angular.module('metadatamanagementApp')
  .controller('ConceptDetailController', [
  'entity',
  'PageMetadataService',
  'LanguageService',
  '$state',
  'BreadcrumbService',
  'Principal',
  'ConceptAttachmentResource',
  'SearchResultNavigatorService',
  '$stateParams',
  '$log',
  'blockUI',
  '$mdSidenav',
    function(entity, PageMetadataService, LanguageService,
             $state, BreadcrumbService, Principal,
             ConceptAttachmentResource, SearchResultNavigatorService,
             $stateParams, $log, blockUI, $mdSidenav) {
      blockUI.start();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();

      var getTags = function(concept) {
        if (concept.tags) {
          var language = LanguageService.getCurrentInstantly();
          return concept.tags[language];
        } else {
          return [];
        }
      };

      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {
        dataPackagesCount: 0,
        surveysCount: 0,
        instrumentsCount: 0,
        questionsCount: 0,
        dataSetsCount: 0,
        variablesCount: 0
      };

      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      ctrl.loadAttachments = function() {
        ConceptAttachmentResource.findByConceptId({
          conceptId: ctrl.concept.id
        }).$promise.then(
          function(attachments) {
            if (attachments.length > 0) {
              ctrl.attachments = attachments;
            }
          });
      };

      entity.promise.then(function(result) {
        PageMetadataService.setPageTitle('concept-management.detail.title', {
          title: result.title[LanguageService.getCurrentInstantly()]
        });
        PageMetadataService.setPageDescription(
          'concept-management.detail.page-description', {
          description: result.description[LanguageService.getCurrentInstantly()]
        });
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id
        });

        ctrl.concept = result;
        ctrl.loadAttachments();

        ctrl.conceptTags = getTags(result);
      }, $log.error).finally(blockUI.stop);

      ctrl.conceptEdit = function() {
        $state.go('conceptEdit', {id: ctrl.concept.id});
      };

      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };
    }]);

