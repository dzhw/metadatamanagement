/* global _*/
'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyDetailController',
    function(entity, PageTitleService, LanguageService, DataSetSearchService,
      $state, ToolbarHeaderService, Principal, SimpleMessageToastService,
      StudyAttachmentResource) {
      var ctrl = this;
      ctrl.counts = {};
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
        if (result.release || Principal.hasAuthority('ROLE_PUBLISHER')) {
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
          DataSetSearchService.findByStudyId(result.id,
            ['id', 'number', 'description', 'subDataSets'])
            .then(function(dataSets) {
              ctrl.dataSets = [];
              dataSets.hits.hits.forEach(function(dataSet) {
                ctrl.dataSets.push({
                  'id': dataSet._source.id,
                  'number': dataSet._source.number,
                  'description': dataSet._source.description,
                  'accessWays': _.map(dataSet._source.subDataSets,
                    'accessWay').join(', '),
                  'maxOfNumberOfObservations': _.maxBy(dataSet._source.
                    subDataSets, function(subDataSet) {
                      return subDataSet.numberOfObservations;})
                      .numberOfObservations
                });
              });
            });
          StudyAttachmentResource.findByStudyId({
              id: ctrl.study.id
            }).$promise.then(
              function(attachments) {
                if (attachments.length > 0) {
                  console.log(attachments);
                  ctrl.attachments = attachments;
                }
              });
        } else {
          SimpleMessageToastService.openSimpleMessageToast(
          'study-management.detail.not-released-toast', {id: result.id}
          );
        }
      });
    });
