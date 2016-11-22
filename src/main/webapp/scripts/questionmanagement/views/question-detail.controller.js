/* global html_beautify */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function(StudyReferencedResource,
      VariableSearchDialogService, entity, $state,
      SimpleMessageToastService, QuestionSearchService, CleanJSObjectService,
      RelatedPublicationSearchDialogService, VariableSearchService,
      RelatedPublicationSearchService, InstrumentSearchService,
      PageTitleService, LanguageService) {

      var ctrl = this;
      this.representationCodeToggleFlag = true;
      ctrl.question = entity;
      ctrl.predecessors = [];
      ctrl.successors = [];
      ctrl.counts = {};

      entity.$promise.then(function() {
        ctrl.questionIdAsArray = ctrl.question.id.split(',');
        QuestionSearchService.findPredeccessors(ctrl.question.id)
          .then(function(predecessors) {
            if (!CleanJSObjectService.isNullOrEmpty(predecessors)) {
              ctrl.predecessors = predecessors.hits.hits;
            }
          });
        QuestionSearchService.findSuccessors(ctrl.question.successors)
          .then(function(successors) {
            if (!CleanJSObjectService.isNullOrEmpty(successors)) {
              ctrl.successors = successors.docs;
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
            html_beautify(ctrl.question.technicalRepresentation.source); //jscs:ignore
          }
        }

        StudyReferencedResource
          .getReferencedStudy({
            id: ctrl.question.dataAcquisitionProjectId
          })
          .$promise.then(function(study) {
            ctrl.study = study;
            if (ctrl.study) {
              PageTitleService.setPageTitle(
                study.title[LanguageService.getCurrentInstantly()] +
                ' ' +
                ctrl.question.number);
            }
          });
        VariableSearchService.countBy('questionId',
          ctrl.question.id).then(function(variablesCount) {
          ctrl.counts.variablesCount = variablesCount.count;
        });
        RelatedPublicationSearchService.countBy('questionIds',
          ctrl.question.id).then(function(publicationsCount) {
          ctrl.counts.publicationsCount = publicationsCount.count;
        });
        InstrumentSearchService.findBySurveyId(ctrl.question.surveyId)
          .then(function(instrument) {
            if (instrument.hits.hits.length > 0) {
              ctrl.instrument = instrument.hits.hits[0]._source;
            }
          });
      });
      ctrl.showRelatedVariables = function() {
        var paramObject = {};
        paramObject.methodName = 'findByQuestionId';
        paramObject.methodParams = ctrl.question.id;
        VariableSearchDialogService.findVariables(paramObject);
      };
      ctrl.showRelatedPublications = function() {
        var paramObject = {};
        paramObject.methodName = 'findByQuestionId';
        paramObject.methodParams = ctrl.question.id;
        RelatedPublicationSearchDialogService.
        findRelatedPublications(paramObject);
      };
      ctrl.openSuccessCopyToClipboardToast = function(message) {
        SimpleMessageToastService.openSimpleMessageToast(message, []);
      };
      ctrl.toggleRepresentationCode = function() {
        ctrl.representationCodeToggleFlag = !ctrl.representationCodeToggleFlag;
      };
    });
