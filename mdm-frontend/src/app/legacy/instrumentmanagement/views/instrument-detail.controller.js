/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentDetailController', [
  'entity',
  'InstrumentAttachmentResource',
  'MessageBus',
  'PageMetadataService',
  'LanguageService',
  '$state',
  'CleanJSObjectService',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'SearchResultNavigatorService',
  'DataAcquisitionProjectResource',
  'ProjectUpdateAccessService',
  'InstrumentSearchService',
  'OutdatedVersionNotifier',
  '$stateParams',
  'blockUI',
  '$mdSidenav',
  'ContainsOnlyQualitativeDataChecker',
  '$scope',
  'CitationHintGeneratorService',
  'InstrumentAttachmentCitationDialogService',
  'InstrumentAttachmentTypesEn',
    function(entity, InstrumentAttachmentResource, MessageBus,
             PageMetadataService, LanguageService, $state, CleanJSObjectService,
             BreadcrumbService, Principal, SimpleMessageToastService,
             SearchResultNavigatorService,
             DataAcquisitionProjectResource, ProjectUpdateAccessService,
             InstrumentSearchService, OutdatedVersionNotifier, $stateParams,
             blockUI, $mdSidenav, ContainsOnlyQualitativeDataChecker,
             $scope, citationHintGeneratorService,
             instrumentAttachmentCitationDialogService, InstrumentAttachmentTypesEn) {
      blockUI.start();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();
      var activeProject;
      //Controller Init
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {
        surveysCount: 0,
        questionsCount: 0,
        dataSetsCount: 0,
        conceptsCount: 0
      };
      ctrl.survey = null;
      ctrl.attachments = null;
      ctrl.dataPackage = null;
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      //Wait for instrument Promise
      entity.promise.then(function(result) {
        var fetchFn = InstrumentSearchService.findShadowByIdAndVersion
          .bind(null, result.masterId, null, ['nested*','questions', 'dataSets',
            'variables','relatedPublications','concepts']);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: result.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.project = project;
            ctrl.shouldDisplayEditButton = localStorage.getItem(
              'currentView') != 'orderView' && !(project.release != null && !project.release.isPreRelease);
            ctrl.assigneeGroup = project.assigneeGroup;
            activeProject = project;
            ctrl.isProviderView = localStorage.getItem('currentView') != 'orderView';
          });
        }
        ctrl.onlyQualitativeData = ContainsOnlyQualitativeDataChecker
          .check(result);
        if (!ctrl.onlyQualitativeData) {
          ctrl.counts.variablesCount = 0;
        }
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'number': result.number,
          'instrumentIsPresent': true,
          'surveys': result.surveys,
          'dataPackageId': result.dataPackageId,
          'dataPackageIsPresent': CleanJSObjectService
            .isNullOrEmpty(result.dataPackage) ? false : true,
          'projectId': result.dataAcquisitionProjectId,
          'version': result.shadow ? _.get(result, 'release.version') : null
        });
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onDataPackageChange',
            {
              masterId: result.dataPackage.masterId,
              projectId: result.dataAcquisitionProjectId
            });
        }
        PageMetadataService.setPageTitle('instrument-management.' +
          'detail.page-title', {
          description: result.description[currenLanguage] ?
            result.description[currenLanguage] :
            result.description[secondLanguage]
        });

        PageMetadataService.setPageDescription('instrument-management.' +
          'detail.page-description', {
            title: result.title[currenLanguage] ?
              result.title[currenLanguage] :
              result.title[secondLanguage]
          });

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
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'instrument-management.detail.not-released-toast', {id: result.id}
          );
        }
      }).finally(blockUI.stop);

      ctrl.instrumentEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'instruments', true)) {
          $state.go('instrumentEdit', {id: ctrl.instrument.id});
        }
      };

      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };

      /**
       * Creates a citation hint for the attachment's citation details.
       * @param {InstrumentAttachmentMetadata} attachment the attachment the citation hint is created for
       * @param {string} currentLanguage the language the user is currently using
       * @returns the citation hint
       */
      const createInstrumentAttachmentCitation = function(attachment, currentLanguage) {

        if (!attachment.citationDetails) return;
        if (!attachment.citationDetails.publicationYear) return;
        if (!attachment.citationDetails.location) return;
        if (!attachment.citationDetails.institution) return;
        if (!attachment.citationDetails.authors) return;
        if (attachment.citationDetails.authors.length === 0) return;

        const fallbackLanguage = currentLanguage === 'de' ? 'en' : 'de';
        var description = "";
        if (attachment.language === currentLanguage) {
          description = attachment.description[currentLanguage];
        } else if (!!attachment.description[fallbackLanguage]) {
          description = attachment.description[fallbackLanguage];
        } else {
          return;
        }

        return citationHintGeneratorService.generateCitationHintForInstrumentAttachment(
          attachment.citationDetails, description);
      }

      /**
       * Assemble list of citation details for the instrument attachments
       * and open a dialog to present them to the user.
       */
      const openInstrumentAttachmentCitationDialog = function() {
        const currentLanguage = LanguageService.getCurrentInstantly();
        const citationItems = [];
        // add citation details for questionnaires if there are any
        citationItems.push(...ctrl.attachments
          .filter(a => a.type.en === InstrumentAttachmentTypesEn.Questionnaire)
          .map(a => ({
            hint: createInstrumentAttachmentCitation(a, currentLanguage),
            details: a.citationDetails
          }))
          .filter(a => !!a.hint));
        citationItems.filter()
        // add citation details for variable questionnaires
        // if there are none for regular questionnaires
        if (citationItems.length === 0) {
          citationItems.push(...ctrl.attachments
            .filter(a => a.type.en === InstrumentAttachmentTypesEn.VariableQuestionnaire)
            .map(a => ({
              hint: createInstrumentAttachmentCitation(a, currentLanguage),
              details: a.citationDetails
            }))
            .filter(a => !!a.hint));
          }
        instrumentAttachmentCitationDialogService.openDialog(citationItems);
      }

      // show citation dialog when event is triggered
      // (clicking the button in the common details subview)
      $scope.$on("open-instrument-citation-dialog", () => openInstrumentAttachmentCitationDialog());
    }]);

