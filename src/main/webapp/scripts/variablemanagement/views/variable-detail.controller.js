/* global html_beautify, _*/
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function(entity,
    MessageBus,
    VariableSearchService, Principal,
    SimpleMessageToastService,
    PageMetadataService, LanguageService,
    CleanJSObjectService,
    $state, BreadcrumbService,
    SearchResultNavigatorService,
    OutdatedVersionNotifier, VariableRepositoryClient,
    $stateParams, blockUI, $mdSidenav) {
    blockUI.start();
    SearchResultNavigatorService
      .setSearchIndex($stateParams['search-result-index']);

    SearchResultNavigatorService.registerCurrentSearchResult();
    var ctrl = this;
    ctrl.isAuthenticated = Principal.isAuthenticated;
    ctrl.hasAuthority = Principal.hasAuthority;
    ctrl.searchResultIndex = SearchResultNavigatorService
      .getSearchIndex();
    ctrl.generationCodeToggleFlag = true;
    ctrl.filterDetailsCodeToggleFlag = true;
    ctrl.notAllRowsVisible = true;
    ctrl.counts = {
      surveysCount: 0,
      dataSetsCount: 0,
      questionsCount: 0,
      conceptsCount: 0
    };
    ctrl.nextVariables = [];
    ctrl.previousVariables = [];
    ctrl.validResponsesOrMissingsAvailable = false;
    ctrl.enableJsonView = Principal
      .hasAnyAuthority(['ROLE_PUBLISHER','ROLE_ADMIN']);

    entity.promise.then(function(result) {
      var fetchFn = VariableSearchService.findShadowByIdAndVersion
        .bind(null, result.masterId, null, ['nested*','questions',
          'instruments','relatedPublications','concepts']);
      OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);
      var currenLanguage = LanguageService.getCurrentInstantly();
      var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
      PageMetadataService.setPageTitle('variable-management.detail.title', {
        label: result.label[currenLanguage] ? result.label[
          currenLanguage] : result.label[secondLanguage]
      });
      BreadcrumbService.updateToolbarHeader({
        'stateName': $state.current.name,
        'id': result.id,
        'name': result.name,
        'dataSetId': result.dataSetId,
        'dataSetNumber': result.dataSetNumber,
        'dataSetIsPresent': !CleanJSObjectService.isNullOrEmpty(result.dataSet),
        'surveys': result.surveys,
        'dataPackageId': result.dataPackageId,
        'dataPackageIsPresent': !CleanJSObjectService
          .isNullOrEmpty(result.dataPackage),
        'projectId': result.dataAcquisitionProjectId,
        'version': result.shadow ? _.get(result, 'release.version') : null
      });
      if (result.release || Principal.hasAnyAuthority(['ROLE_PUBLISHER',
          'ROLE_DATA_PROVIDER'])) {
        ctrl.variable = result;
        if (ctrl.variable.distribution != null && (
            !CleanJSObjectService.isNullOrEmpty(ctrl
              .variable.distribution.missings) ||
            !CleanJSObjectService.isNullOrEmpty(ctrl
              .variable.distribution.validResponses))) {
          ctrl.validResponsesOrMissingsAvailable = true;
        }
        ctrl.dataPackage = ctrl.variable.dataPackage;
        ctrl.dataSet = ctrl.variable.dataSet;

        //Find previousVariables
        var previousIndexInDataSet = result.indexInDataSet - 1;
        VariableSearchService.findByDataSetIdAndIndexInDataSet(result.dataSetId,
          previousIndexInDataSet, ['id', 'label', 'name', 'dataType',
            'scaleLevel', 'surveys', 'masterId', 'release', 'shadow'])
          .then(function(resultPreviousVariable) {
            ctrl.previousVariables = resultPreviousVariable.hits.hits;
          });

        //Find nextVariables
        var nextIndexInDataSet = result.indexInDataSet + 1;
        VariableSearchService.findByDataSetIdAndIndexInDataSet(result.dataSetId,
          nextIndexInDataSet, ['id', 'label', 'name', 'dataType',
            'scaleLevel', 'surveys', 'masterId', 'release', 'shadow'])
          .then(function(resultNextVariable) {
            ctrl.nextVariables = resultNextVariable.hits.hits;
          });

        if (ctrl.variable.filterDetails &&
            ctrl.variable.filterDetails.expression) {
          html_beautify(ctrl.variable.filterDetails.expression); //jscs:ignore
        }
        if (ctrl.variable.generationDetails) {
          html_beautify(ctrl.variable.generationDetails.rule); //jscs:ignore
        }
      } else {
        SimpleMessageToastService.openAlertMessageToast(
          'variable-management.detail.not-released-toast', {
            id: result.id
          }
        );
      }
      if (!Principal.isAuthenticated()) {
        MessageBus.set('onDataPackageChange',
          {
            masterId: result.dataPackage.masterId
          });
      }
      if (result.repeatedMeasurementIdentifier) {
        VariableRepositoryClient
          .findAllByRepeatedMeasurementIdentifierAndDataSetIdAndIdNot(
          result.repeatedMeasurementIdentifier, result.dataSetId, result.id
        ).then(function(response) {
          ctrl.repeatedMeasurementVariables = response.data;
        });
      }
      if (result.derivedVariablesIdentifier) {
        VariableRepositoryClient
          .findAllByDerivedVariablesIdentifierAndDataSetIdAndIdNot(
          result.derivedVariablesIdentifier, result.dataSetId, result.id
        ).then(function(response) {
          ctrl.derivedVariables = response.data;
        });
      }
    }).finally(blockUI.stop);
    ctrl.isRowHidden = function(index) {
      if (index <= 4 || index >= ctrl
        .variable.distribution.validResponses.length - 5) {
        return false;
      } else {
        return ctrl.notAllRowsVisible;
      }
    };
    ctrl.toggleAllRowsVisible = function() {
      ctrl.notAllRowsVisible = !ctrl.notAllRowsVisible;
    };
    ctrl.toggleGenerationCode = function() {
      ctrl.generationCodeToggleFlag = !ctrl.generationCodeToggleFlag;
    };
    ctrl.toggleFilterDetailsCode = function() {
      ctrl.filterDetailsCodeToggleFlag = !ctrl.filterDetailsCodeToggleFlag;
    };
    ctrl.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []);
    };

    /* Show headline for Central Tendency,
      if one element is filled with data. */
    ctrl.checkCentralTendencyElements = function() {

      return ctrl.variable.distribution != null &&
        ctrl.variable.distribution.statistics != null &&
        (ctrl.variable.distribution.statistics.meanValue != null ||
          ctrl.variable.distribution.statistics.median != null ||
          ctrl.variable.distribution.statistics.mode != null);
    };

    /* Show headline for Dispersion, if one element is filled with data. */
    ctrl.checkDispersionElements = function() {
      return ctrl.variable.distribution != null &&
        ctrl.variable.distribution.statistics != null &&
        ctrl.variable.distribution.statistics.standardDeviation;
    };

    /* Show headline for Distribution, if one element is filled with data. */
    ctrl.checkDistributionElements = function() {
      return ctrl.variable.distribution != null &&
        ctrl.variable.distribution.statistics != null &&
        (ctrl.variable.distribution.statistics.skewness != null ||
          ctrl.variable.distribution.statistics.kurtosis != null);
    };

    ctrl.isDiagramVisible = function() {
      return ctrl.variable.distribution != null &&
        ctrl.variable.distribution.validResponses &&
        ctrl.variable.distribution.validResponses.length > 0;
    };

    ctrl.toggleSidenav = function() {
      $mdSidenav('SideNavBar').toggle();
    };

    ctrl.onPanelVariableSelected = function(selectedVariable) {
      if (selectedVariable.id) {
        $state.go('variableDetail', {id: selectedVariable.masterId,
          version: ctrl.variable.shadow ? ctrl.variable.release.version : '',
        },
        {reload: true, notifiy: true});
      } else {
        $state.go('dataPackageDetail', {id: ctrl.variable.dataPackage.masterId,
          version: ctrl.variable.shadow ? ctrl.variable.release.version : '',
          'repeated-measurement-identifier': ctrl.variable
            .repeatedMeasurementIdentifier,
          type: 'variables'
        });
      }
    };

    ctrl.onDerivedVariableSelected = function(selectedVariable) {
      if (selectedVariable.id) {
        $state.go('variableDetail', {id: selectedVariable.masterId,
          version: ctrl.variable.shadow ? ctrl.variable.release.version : '',
        },
        {reload: true, notifiy: true});
      } else {
        $state.go('dataPackageDetail', {id: ctrl.variable.dataPackage.masterId,
          version: ctrl.variable.shadow ? ctrl.variable.release.version : '',
          'derived-variables-identifier':
            ctrl.variable.derivedVariablesIdentifier,
          type: 'variables'
        });
      }
    };
  });
