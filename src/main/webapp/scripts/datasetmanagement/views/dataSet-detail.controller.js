'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetDetailController',
    function(entity, Principal,
      VariableSearchService,
      DataSetSearchService, DataSetReportService, PageTitleService,
      LanguageService, $state, ToolbarHeaderService, CleanJSObjectService,
      SimpleMessageToastService, DataSetAttachmentResource,
      DataSetCitateDialogService, SearchResultNavigatorService, $stateParams) {
      SearchResultNavigatorService.registerCurrentSearchResult(
          $stateParams['search-result-index']);
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.counts = {};

      ctrl.openDialog = function(subDataSet) {
        DataSetCitateDialogService.showDialog(subDataSet.citationHint);
      };

      entity.promise.then(function(result) {
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        PageTitleService.setPageTitle('data-set-management.detail.title', {
          description: result.description[currenLanguage] ? result
          .description[currenLanguage] : result.description[secondLanguage],
          dataSetId: result.id
        });
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'number': result.number,
          'studyId': result.studyId,
          'surveys': result.surveys,
          'dataSetIsPresent': true,
          'studyIsPresent': CleanJSObjectService.
          isNullOrEmpty(result.study) ? false : true,
          'projectId': result.dataAcquisitionProjectId});
        if (result.release || Principal.hasAuthority('ROLE_PUBLISHER')) {
          ctrl.dataSet = result;
          ctrl.study = result.study;
          ctrl.counts.surveysCount = result.surveys.length;
          if (ctrl.counts.surveysCount === 1) {
            ctrl.survey = result.surveys[0];
          }
          ctrl.counts.variablesCount = result.variables.length;
          if (ctrl.counts.variablesCount === 1) {
            ctrl.variable = result.variables[0];
          }
          ctrl.counts.publicationsCount = result.relatedPublications.length;
          if (ctrl.counts.publicationsCount === 1) {
            ctrl.relatedPublication = result.relatedPublications[0];
          }
          DataSetSearchService
          .countBy('dataAcquisitionProjectId',
          ctrl.dataSet.dataAcquisitionProjectId)
          .then(function(dataSetsCount) {
            ctrl.counts.dataSetsCount = dataSetsCount.count;
          });
          ctrl.dataSet.subDataSets.forEach(function(subDataSet) {
            VariableSearchService.countBy('accessWays',
            subDataSet.accessWay, ctrl.dataSet.id).then(function(counts) {
              ctrl.counts[subDataSet.name] = counts.count;
            });
          });
          DataSetAttachmentResource.findByDataSetId({
            id: ctrl.dataSet.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });
        } else {
          SimpleMessageToastService.openSimpleMessageToast(
          'data-set-management.detail.not-released-toast', {id: result.id}
          );
        }
      });
      ctrl.uploadTexTemplate = function(files) {
        if (files != null) {
          DataSetReportService.uploadTexTemplate(files, ctrl.dataSet.id);
        }
      };
    });
