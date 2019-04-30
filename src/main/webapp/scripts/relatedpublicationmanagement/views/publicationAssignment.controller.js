/* global $ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('PublicationAssignmentController',
    function(CurrentProjectService, StudyIdBuilderService,
      RelatedPublicationSearchService, DataAcquisitionProjectResource,
      LanguageService, $timeout, SimpleMessageToastService, $state,
      ProjectUpdateAccessService, PageTitleService, ToolbarHeaderService, $q,
      SearchDao, PublicationAssignmentResource, ElasticSearchAdminService,
      $scope, DeleteMetadataService, blockUI) {
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
        blockUI.start();
        ctrl.project = CurrentProjectService.getCurrentProject();
        ctrl.studyId = StudyIdBuilderService.buildStudyId(ctrl.project.id);
        ctrl.publications = [];
        ctrl.searchCache = {};

        DataAcquisitionProjectResource.get({
          id: ctrl.project.id
        }).$promise.then(function(project) {
          ctrl.project = project;
          if (project.release != null) {
            blockUI.stop();
            handleReleasedProject();
          } else if (!ProjectUpdateAccessService
              .isUpdateAllowed(project, 'publications', true)) {
            blockUI.stop();
            redirectToSearchView();
          } else {
            RelatedPublicationSearchService.findByStudyId(
              ctrl.studyId, ['id','title'], 0, 100).then(function(result) {
                result.hits.hits.forEach(function(hit) {
                  ctrl.publications.push(hit._source);
                });
              }).finally(blockUI.stop);
            updateToolbarHeaderAndPageTitle();
          }
        });
      };

      $scope.$on('current-project-changed',
        function(event, changedProject) { // jshint ignore:line
          if (changedProject) {
            ctrl.init();
          }
        });

      ctrl.selectedPublicationChanged = function(publication) {
        if (publication) {
          PublicationAssignmentResource.save({
            studyId: ctrl.studyId,
            publicationId: publication.id
          }).$promise.then(ctrl.updateElasticSearchIndex).then(function() {
            ctrl.publications.push(publication);
            ctrl.searchText = '';
            ctrl.selectedPublication = null;
            ctrl.preventSearching = true;
            ctrl.searchCache = {};
            $(':focus').blur();
          });
        }
      };

      ctrl.searchPublications = function(searchText) {
        var deferred = $q.defer();
        var idsToExclude = ctrl.publications.map(function(publication) {
          return publication.id;
        });
        if (ctrl.preventSearching) {
          ctrl.preventSearching = false;
          deferred.resolve([]);
          return deferred.promise;
        }
        if (ctrl.searchCache['text_' + searchText]) {
          deferred.resolve(ctrl.searchCache['text_' + searchText]);
          return deferred.promise;
        }
        SearchDao.search(searchText, 1, null, null, 'related_publications',
         100, idsToExclude).then(function(response) {
          ctrl.searchCache['text_' + searchText] = response.hits.hits.map(
            function(searchResult) {
            var publication = {
              id: searchResult._source.id,
              title: searchResult._source.title
            };
            return publication;
          });
          deferred.resolve(ctrl.searchCache['text_' + searchText]);
        }).catch(function() {
          deferred.reject();
        });

        return deferred.promise;
      };

      ctrl.updateElasticSearchIndex = function() {
        return ElasticSearchAdminService
          .processUpdateQueue('related_publications');
      };

      ctrl.removePublication = function(publication) {
        PublicationAssignmentResource.delete({
          studyId: ctrl.studyId,
          publicationId: publication.id
        }).$promise.then(ctrl.updateElasticSearchIndex).then(function() {
          ctrl.publications = ctrl.publications
          .filter(function(item) {
            return item.id !== publication.id;
          });
          ctrl.searchCache = {};
        });
      };

      ctrl.removeAllPublications = function() {
        DeleteMetadataService.deleteAllOfType(ctrl.project,
          'publications');
      };

      $scope.$on('deletion-completed', function() {
        ctrl.publications = [];
        ctrl.searchCache = {};
      });

      ctrl.init();
    });
