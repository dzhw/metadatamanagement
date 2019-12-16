'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function(entity,
             MessageBus,
             PageTitleService,
             LanguageService,
             $state, $location,
             ToolbarHeaderService, Principal, SimpleMessageToastService,
             SearchResultNavigatorService,
             $stateParams,
             DataAcquisitionProjectAttachmentsResource,
             $rootScope, DataAcquisitionProjectResource,
             ProjectUpdateAccessService, $scope,
             $timeout, $document,
             OutdatedVersionNotifier, StudySearchService, $log,
             blockUI) {
      blockUI.start();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();

      var getTags = function(study) {
        if (study.tags) {
          var language = LanguageService.getCurrentInstantly();
          return study.tags[language];
        } else {
          return [];
        }
      };
      var ctrl = this;
      var activeProject;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {
        surveysCount: 0,
        instrumentsCount: 0,
        questionsCount: 0,
        dataSetsCount: 0,
        variablesCount: 0,
        publicationsCount: 0,
        conceptsCount: 0
      };
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);
      var bowser = $rootScope.bowser;

      ctrl.loadAttachments = function() {
        DataAcquisitionProjectAttachmentsResource.get({
          id: ctrl.study.dataAcquisitionProjectId
        }).$promise.then(
          function(attachments) {
            if (attachments) {
              ctrl.attachments = attachments;
            }
          });
      };

      ctrl.isBetaRelease = function(study) {
        if (study.release) {
          return bowser.compareVersions(['1.0.0', study.release.version]) === 1;
        }
        return false;
      };

      $scope.$on('deletion-completed', function() {
        //wait for 2 seconds until refresh
        //in order to wait for elasticsearch reindex
        $timeout($state.reload, 2000);
      });

      entity.promise.then(function(result) {
        var fetchFn = StudySearchService.findShadowByIdAndVersion
          .bind(null, result.masterId, null, ['nested*','variables','questions',
            'surveys','instruments', 'dataSets', 'relatedPublications',
            'concepts']);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: result.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.projectIsCurrentlyReleased = (project.release != null);
            ctrl.assigneeGroup = project.assigneeGroup;
            activeProject = project;
          });
        }
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onDataPackageChange',
            {
              masterId: result.masterId,
              version: result.release.version
            });
        }

        PageTitleService.setPageTitle('study-management.detail.title', {
          title: result.title[LanguageService.getCurrentInstantly()],
          studyId: result.id
        });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'studyIsPresent': true,
          'projectId': result.dataAcquisitionProjectId
        });
        if (result.release || Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.study = result;
          ctrl.loadAttachments();

          $timeout(function() {
            if ($location.search().query) {
              ctrl.scroll();
            }
          }, 500);
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'study-management.detail.not-released-toast', {id: result.id}
          );
        }

        ctrl.studyTags = getTags(result);

      }, $log.error).finally(blockUI.stop);

      ctrl.scroll = function() {
        var element = $document[0].getElementById('related-objects');
        if ($rootScope.bowser.msie) {
          element.scrollIntoView(true);
        } else {
          element.scrollIntoView({behavior: 'smooth', inline: 'nearest'});
        }
      };
      ctrl.studyEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'studies', true)) {
          $state.go('studyEdit', {id: ctrl.study.id});
        }
      };
    });
