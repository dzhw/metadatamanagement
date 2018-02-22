'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function(entity, PageTitleService, LanguageService, DataSetSearchService,
      $state, ToolbarHeaderService, Principal, SimpleMessageToastService,
      StudyAttachmentResource, SearchResultNavigatorService, $stateParams,
      $rootScope) {
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

      ctrl.isBetaRelease = function() {
        //TODO DKatzberg Fake Check ...
        var checkForBetaRelease = bowser.compareVersions(['1.0.0', '1.5.1']);
        return checkForBetaRelease === 1;
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

          //TODO DKatzberg Fake Check, need info in search document
          actualVersion = '1.5.1';
          if (bowser.compareVersions([versionFromUrl, actualVersion]) === -1) {
            SimpleMessageToastService.openSimpleMessageToast(
              'study-management.detail.old-version',
              {
                id: result.id,
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
