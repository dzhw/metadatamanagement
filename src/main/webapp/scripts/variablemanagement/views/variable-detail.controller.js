/* global html_beautify, _*/
/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', function($scope, entity,
    QuestionSearchService, VariableSearchService, Principal,
    SimpleMessageToastService, PageTitleService, LanguageService,
    CleanJSObjectService, $state, ToolbarHeaderService,
    SearchResultNavigatorService, ProductChooserDialogService,
    OutdatedVersionNotifier, $stateParams, blockUI) {
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
    ctrl.counts = {};
    ctrl.nextVariables = [];
    ctrl.previousVariables = [];
    ctrl.validResponsesOrMissingsAvailable = false;
    ctrl.enableJsonView = Principal
      .hasAnyAuthority(['ROLE_PUBLISHER','ROLE_ADMIN']);
    ctrl.jsonExcludes = [
      'nestedDataSet',
      'nestedStudy',
      'nestedQuestions',
      'nestedRelatedPublications',
      'nestedSurveys',
      'nestedInstruments',
      'nestedConcepts'
    ];
    entity.promise.then(function(result) {
      var fetchFn = VariableSearchService.findShadowByIdAndVersion
        .bind(null, result.masterId);
      OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

      var currenLanguage = LanguageService.getCurrentInstantly();
      var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
      PageTitleService.setPageTitle('variable-management.detail.title', {
        label: result.label[currenLanguage] ? result.label[
          currenLanguage] : result.label[secondLanguage],
        variableId: result.id
      });
      ToolbarHeaderService.updateToolbarHeader({
        'stateName': $state.current.name,
        'id': result.id,
        'name': result.name,
        'dataSetId': result.dataSetId,
        'dataSetNumber': result.dataSetNumber,
        'dataSetIsPresent': CleanJSObjectService.
        isNullOrEmpty(result.dataSet) ? false : true,
        'surveys': result.surveys,
        'studyId': result.studyId,
        'studyIsPresent': CleanJSObjectService.
        isNullOrEmpty(result.study) ? false : true,
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
        ctrl.study = ctrl.variable.study;
        ctrl.dataSet = ctrl.variable.dataSet;

        ctrl.counts.questionsCount = ctrl.variable.relatedQuestions ?
          ctrl.variable.relatedQuestions.length : 0;
        if (ctrl.counts.questionsCount === 1) {
          QuestionSearchService
            .findByVariableId(ctrl.variable.id, ['number',
              'instrumentNumber',
              'questionText', 'id'
            ])
            .then(function(question) {
              ctrl.question = question.hits.hits[0]._source;
            });
        }

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

        ctrl.counts.surveysCount = ctrl.variable.surveyNumbers ?
          ctrl.variable.surveyNumbers.length : 0;
        if (ctrl.counts.surveysCount === 1) {
          ctrl.survey = ctrl.variable.surveys[0];
        }
        ctrl.counts.conceptsCount = ctrl.variable.concepts.length;
        if (ctrl.counts.conceptsCount === 1) {
          ctrl.concept = ctrl.variable.concepts[0];
        }
        if (ctrl.variable.panelIdentifier) {
          VariableSearchService
            .countBy('panelIdentifier', ctrl.variable.panelIdentifier,
              null, _.get(result, 'release.version'))
            .then(function(variablesInPanel) {
              ctrl.counts.variablesInPanel = variablesInPanel.count;
            });
        } else {
          ctrl.counts.variablesInPanel = 0;
        }
        if (ctrl.variable.derivedVariablesIdentifier) {
          VariableSearchService
            .countBy('derivedVariablesIdentifier',
              ctrl.variable.derivedVariablesIdentifier, null,
              _.get(result, 'release.version'))
            .then(function(derivedVariables) {
              ctrl.counts.derivedVariables = derivedVariables.count;
            });
        } else {
          ctrl.counts.derivedVariables = 0;
        }
        ctrl.counts.publicationsCount = ctrl.variable
          .relatedPublications.length;
        if (ctrl.counts.publicationsCount === 1) {
          ctrl.relatedPublication = ctrl.variable
            .relatedPublications[0];
        }
        if (ctrl.variable.filterDetails) {
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

    ctrl.addToShoppingCart = function(event) {
      ProductChooserDialogService.showDialog(
        ctrl.variable.dataAcquisitionProjectId, ctrl.variable.accessWays,
        ctrl.variable.study, ctrl.variable.release.version,
        event);
    };
  });
