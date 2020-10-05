/* global html_beautify */
/* global _ */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',
    function(entity, $state, BreadcrumbService, MessageBus,
      SimpleMessageToastService, QuestionSearchService, CleanJSObjectService,
      PageTitleService, $rootScope, Principal, SearchResultNavigatorService,
      QuestionImageMetadataResource, $mdMenu, $timeout, $stateParams,
      OutdatedVersionNotifier, blockUI, LocationSimplifier, $mdSidenav) {
      blockUI.start();
      LocationSimplifier.removeDollarSign();
      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      this.representationCodeToggleFlag = true;
      ctrl.predecessors = [];
      ctrl.successors = [];
      ctrl.counts = {
        surveysCount: 0,
        instrumentsCount: 0,
        variablesCount: 0,
        conceptsCount: 0
      };
      ctrl.currentImageIndex = 0;
      ctrl.currentImageLanguage = '';
      ctrl.imageLanguages = [];
      ctrl.imagesGroupedByLanguage = {};

      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      entity.promise.then(function(result) {
        var fetchFn = QuestionSearchService.findShadowByIdAndVersion
          .bind(null, result.masterId, null, ['nested*', 'dataSets',
              'variables','relatedPublications','concepts']);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

        var title = {
          questionNumber: result.number,
          questionId: result.id
        };
        if (_.isObject(result.instrument)) {
          title.instrumentDescription = result.instrument.
          description[$rootScope.currentLanguage];
        }
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onDataPackageChange',
            {
              masterId: result.dataPackage.masterId,
              version: result.release.version
            });
        }
        PageTitleService.
          setPageTitle('question-management.detail.title', title);
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'questionNumber': result.number,
          'instrumentNumber': result.instrumentNumber,
          'instrumentId': result.instrumentId,
          'instrumentIsPresent': !CleanJSObjectService
                                    .isNullOrEmpty(result.instrument),
          'surveys': result.surveys,
          'dataPackageId': result.dataPackageId,
          'dataPackageIsPresent': !CleanJSObjectService
            .isNullOrEmpty(result.dataPackage),
          'projectId': result.dataAcquisitionProjectId,
          'version': result.shadow ? _.get(result, 'release.version') : null
        });
        if (result.release || Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.question = result;
          QuestionSearchService.findAllPredeccessors(ctrl.question.id, ['id',
            'instrumentNumber', 'questionText', 'type', 'masterId', 'shadow',
            'number', 'dataAcquisitionProjectId', 'instrument.description',
            'release'],
            0, 100)
          .then(function(predecessors) {
            if (!CleanJSObjectService.isNullOrEmpty(predecessors)) {
              ctrl.predecessors = predecessors.hits.hits;
            }
          });
          if (ctrl.question.successors) {
            QuestionSearchService.findAllSuccessors(ctrl.question.successors,
              ['id', 'instrumentNumber', 'questionText', 'type',
              'number', 'dataAcquisitionProjectId', 'masterId', 'shadow',
              'instrument.description', 'release'], 0, 100)
            .then(function(successors) {
              ctrl.successors = successors.hits.hits;
            });
          }
          QuestionImageMetadataResource.findByQuestionId({
            id: ctrl.question.id
          }).$promise.then(
            function(images) {
              if (images.length > 0) {
                images.forEach(function(metadata) {
                  if (ctrl.imageLanguages.indexOf(metadata.language) === -1) {
                    ctrl.imageLanguages.push(metadata.language);
                    ctrl.imagesGroupedByLanguage[metadata.language] = [];
                  }
                  ctrl.imagesGroupedByLanguage[metadata.language]
                    .push(metadata);
                });
                if (ctrl.imageLanguages
                  .indexOf($rootScope.currentLanguage) !== -1) {
                  var index =
                    ctrl.imageLanguages.indexOf($rootScope.currentLanguage);
                  ctrl.currentImageLanguage =
                    ctrl.imageLanguages[index];
                } else {
                  ctrl.currentImageLanguage = ctrl.imageLanguages[0];
                }
              }
            });
          if (ctrl.question.technicalRepresentation) {
            //default value is no beautify
            ctrl.technicalRepresentationBeauty =
            ctrl.question.technicalRepresentation.source;

            //beautify xml, html, xhtml files.
            if (ctrl.question.technicalRepresentation.language === 'xml' ||
            ctrl.question.technicalRepresentation.language === 'xhtml' ||
            ctrl.question.technicalRepresentation.language === 'html') {
              html_beautify(ctrl.technicalRepresentationBeauty); //jscs:ignore
            }
          }
          ctrl.dataPackage = result.dataPackage;
          ctrl.instrument = result.instrument;
        } else {
          SimpleMessageToastService.openAlertMessageToast(
          'question-management.detail.not-released-toast', {id: result.id}
          );
        }
      }).finally(blockUI.stop);

      ctrl.changeCurrentImageLanguage = function(language) {
        if (ctrl.imageLanguages.indexOf(language) !== -1) {
          ctrl.currentImageLanguage = language;
          ctrl.currentImageIndex = 0;
          $timeout($mdMenu.hide);
        }
      };

      ctrl.openSuccessCopyToClipboardToast = function(message) {
        SimpleMessageToastService.openSimpleMessageToast(message, []);
      };

      ctrl.toggleRepresentationCode = function() {
        ctrl.representationCodeToggleFlag = !ctrl.representationCodeToggleFlag;
      };

      ctrl.toggleSidenav = function() {
        $mdSidenav('SideNavBar').toggle();
      };
    });
