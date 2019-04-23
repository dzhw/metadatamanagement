'use strict';

angular.module('metadatamanagementApp')
  .controller('PublicationAssignmentController',
    function(CurrentProjectService, StudyIdBuilderService,
      RelatedPublicationSearchService, DataAcquisitionProjectResource,
      LanguageService, $timeout, SimpleMessageToastService, $state,
      ProjectUpdateAccessService, PageTitleService, ToolbarHeaderService) {
      var ctrl = this;

      var redirectToSearchView = function() {
        $timeout(function() {
          $state.go('search', {
            lang: LanguageService.getCurrentInstantly(),
            type: 'related_publications'
          });
        }, 1000);
      };

      var handleReleasedProject = function() {
        SimpleMessageToastService.openAlertMessageToast(
          'related-publication-management.' +
          'assign.choose-unreleased-project-toast');
        redirectToSearchView();
      };

      var updateToolbarHeaderAndPageTitle = function() {
        PageTitleService.setPageTitle(
          'related-publication-management.assign.page-title', {
            projectId: ctrl.project.id
          });

        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.project.id,
          'projectId': ctrl.project.id,
          'enableLastItem': false
        });
      };

      ctrl.init = function() {
        ctrl.project = CurrentProjectService.getCurrentProject();
        ctrl.studyId = StudyIdBuilderService.buildStudyId(ctrl.project.id);
        ctrl.publications = [];

        DataAcquisitionProjectResource.get({
          id: ctrl.project.id
        }).$promise.then(function(project) {
          ctrl.project = project;
          if (project.release != null) {
            handleReleasedProject();
          } else if (!ProjectUpdateAccessService
              .isUpdateAllowed(project, 'publications', true)) {
            redirectToSearchView();
          } else {
            RelatedPublicationSearchService.findByStudyId(
              ctrl.studyId, ['id','title'], 0, 100).then(function(result) {
                result.hits.hits.forEach(function(hit) {
                  ctrl.publications.push(hit._source);
                });
              });
            updateToolbarHeaderAndPageTitle();
          }
        });
      };

      ctrl.init();
    });
