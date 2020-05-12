'use strict';

angular.module('metadatamanagementApp')
  .controller('ConceptDetailController',
    function(entity, PageTitleService, LanguageService,
             $state, BreadcrumbService, Principal,
             ConceptAttachmentResource, SearchResultNavigatorService,
             $stateParams, $log, blockUI) {
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
        studiesCount: 0,
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
        PageTitleService.setPageTitle('concept-management.detail.title', {
          title: result.title[LanguageService.getCurrentInstantly()],
          conceptId: result.id
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
    });
