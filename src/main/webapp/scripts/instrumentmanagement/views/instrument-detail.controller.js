/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentDetailController',
    function(entity, InstrumentAttachmentResource,
      PageTitleService, LanguageService, $state, CleanJSObjectService,
      ToolbarHeaderService, Principal, SimpleMessageToastService,
      SearchResultNavigatorService, $stateParams, ProductChooserDialogService,
      DataAcquisitionProjectResource, ProjectUpdateAccessService) {
      SearchResultNavigatorService.registerCurrentSearchResult(
        $stateParams['search-result-index']);
      //Controller Init
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = $stateParams['search-result-index'];
      ctrl.survey = null;
      ctrl.attachments = null;
      ctrl.study = null;
      ctrl.questionCount = null;
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER','ROLE_ADMIN']);
      ctrl.isUpdateAllowed = false;

      ctrl.jsonExcludes = [
        'nestedStudy',
        'nestedSurveys',
        'nestedQuestions',
        'nestedVariables',
        'nestedDataSets',
        'nestedRelatedPublications'
      ];
      //Wait for instrument Promise
      entity.promise.then(function(result) {
        if (Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: result.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.projectIsCurrentlyReleased = (project.release != null);
            ctrl.isUpdateAllowed = ProjectUpdateAccessService
              .isUpdateAllowed(project);
          });
        }
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'number': result.number,
          'instrumentIsPresent': true,
          'surveys': result.surveys,
          'studyId': result.studyId,
          'studyIsPresent': CleanJSObjectService.
          isNullOrEmpty(result.study) ? false : true,
          'projectId': result.dataAcquisitionProjectId});
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('instrument-management.' +
        'detail.page-title', {
          description: result.description[currenLanguage] ?
          result.description[currenLanguage] :
          result.description[secondLanguage],
          instrumentId: result.id
        });
        if (result.dataSets) {
          ctrl.accessWays = [];
          result.dataSets.forEach(function(dataSet) {
            ctrl.accessWays = _.union(dataSet.accessWays, ctrl.accessWays);
          });
        }
        if (result.release || Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.instrument = result;
          //load all related objects in parallel
          InstrumentAttachmentResource.findByInstrumentId({
            instrumentId: ctrl.instrument.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });

          ctrl.surveyCount = result.surveys.length;
          if (ctrl.surveyCount === 1) {
            ctrl.survey = result.surveys[0];
          }

          ctrl.study = result.study;

          ctrl.questionCount = result.questions.length;
          if (ctrl.questionCount === 1) {
            ctrl.question = result.questions[0];
          }

          ctrl.publicationCount = result.relatedPublications.length;
          if (ctrl.publicationCount === 1) {
            ctrl.relatedPublication = result.relatedPublications[0];
          }
        } else {
          SimpleMessageToastService.openAlertMessageToast(
          'instrument-management.detail.not-released-toast', {id: result.id}
          );
        }
      });

      ctrl.addToShoppingCart = function(event) {
        ProductChooserDialogService.showDialog(
          ctrl.instrument.dataAcquisitionProjectId, ctrl.accessWays,
          ctrl.instrument.study,
          event);
      };
    });
