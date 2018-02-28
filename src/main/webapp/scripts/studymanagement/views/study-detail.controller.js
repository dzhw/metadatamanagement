'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function(entity, PageTitleService, LanguageService, DataSetSearchService,
      $state, ToolbarHeaderService, Principal, SimpleMessageToastService,
      StudyAttachmentResource, SearchResultNavigatorService, $stateParams,
      $rootScope, LastReleasedProjectVersionService, CleanJSObjectService) {
      SearchResultNavigatorService.registerCurrentSearchResult(
          $stateParams['search-result-index']);
      var versionFromUrl = $stateParams.version;
      var ctrl = this;
      ctrl.searchResultIndex = $stateParams['search-result-index'];
      ctrl.counts = {};
      var bowser = $rootScope.bowser;
      var actualVersion;

      ctrl.loadAttachments = function() {
        StudyAttachmentResource.findByStudyId({
            studyId: ctrl.study.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });
      };

      ctrl.isBetaRelease = function(study) {
        var actualVersion;
        //get actual version or load the last version from history
        if (study.release) {
          actualVersion = study.release.version;
        } else {
          actualVersion =
            LastReleasedProjectVersionService
              .getLastReleasedVersion(study.dataAcquisitionProjectId);
          if (CleanJSObjectService.isNullOrEmpty(actualVersion)) {
            actualVersion = '0.0.0';
          }
        }

        //A check for 0.0.0 string is not necessary!
        var checkForBetaRelease =
          bowser.compareVersions(['1.0.0', actualVersion]);
        return checkForBetaRelease === 1; //1 means 1.0.0 is higher
      };

      entity.promise.then(function(result) {
        PageTitleService.setPageTitle('study-management.detail.title', {
          title: result.title[LanguageService.getCurrentInstantly()],
          studyId: result.id
        });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'studyIsPresent': true,
          'projectId': result.dataAcquisitionProjectId});
        if (result.release || Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.study = result;
          ctrl.counts.surveysCount = result.surveys.length;
          if (ctrl.counts.surveysCount === 1) {
            ctrl.survey = result.surveys[0];
          }
          ctrl.counts.dataSetsCount = result.dataSets.length;
          if (ctrl.counts.dataSetsCount === 1) {
            ctrl.dataSet = result.dataSets[0];
          }
          ctrl.counts.publicationsCount = result.relatedPublications.length;
          if (ctrl.counts.publicationsCount === 1) {
            ctrl.relatedPublication = result.relatedPublications[0];
          }
          ctrl.counts.variablesCount = result.variables.length;
          if (ctrl.counts.variablesCount === 1) {
            ctrl.variable = result.variables[0];
          }
          ctrl.counts.questionsCount = result.questions.length;
          if (ctrl.counts.questionsCount === 1) {
            ctrl.question = result.questions[0];
          }
          ctrl.counts.instrumentsCount = result.instruments.length;
          if (ctrl.counts.instrumentsCount === 1) {
            ctrl.instrument = result.instruments[0];
          }
          /* We need to load search the dataSets cause the contain needed
             survey titles */
          DataSetSearchService.findByStudyId(result.id,
            ['id', 'number', 'description', 'type', 'surveys',
              'maxNumberOfObservations', 'accessWays'])
            .then(function(dataSets) {
              ctrl.dataSets = dataSets.hits.hits;
            });
          ctrl.loadAttachments();

          //get actual version or load the last version from history
          if (result.release) {
            actualVersion = result.release.version;
          } else {
            actualVersion =
              LastReleasedProjectVersionService
                .getLastReleasedVersion(result.dataAcquisitionProjectId);
            if (CleanJSObjectService.isNullOrEmpty(actualVersion)) {
              actualVersion = '0.0.0';
            }
          }

          if (actualVersion !== '0.0.0' &&
            bowser.compareVersions([versionFromUrl, actualVersion]) === -1) {
            SimpleMessageToastService.openSimpleMessageToast(
              'study-management.detail.old-version',
              {
                title: result.title[LanguageService.getCurrentInstantly()],
                versionFromUrl: versionFromUrl,
                actualVersion: actualVersion
              }
            );
          }
        } else {
          SimpleMessageToastService.openSimpleMessageToast(
          'study-management.detail.not-released-toast', {id: result.id}
          );
        }
      });
    });
