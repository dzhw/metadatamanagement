/* global html_beautify */
/* global _ */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function(StudySearchService,
      VariableSearchDialogService, entity,
      SimpleMessageToastService, QuestionSearchService, CleanJSObjectService,
      RelatedPublicationSearchDialogService, VariableSearchService,
      RelatedPublicationSearchService, InstrumentSearchService,
      PageTitleService) {

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
        if (ctrl.question.successors) {
          QuestionSearchService.findSuccessors(ctrl.question.successors)
            .then(function(successors) {
              _.pullAllBy(successors.docs, [{
                'found': false
              }], 'found');
              ctrl.successors = successors.docs;
            });
        }
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
        StudySearchService.findStudy(ctrl.question.dataAcquisitionProjectId)
          .then(function(study) {
            if (study.hits.hits.length > 0) {
              ctrl.study = study.hits.hits[0]._source;
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
        InstrumentSearchService.findInstruments([ctrl.question.instrumentId])
          .then(function(searchResult) {
            var title = {
              questionNumber: ctrl.question.number,
              questionId: ctrl.question.id
            };
            if (searchResult.docs[0].found) {
              ctrl.instrument = searchResult.docs[0]._source;
              title.instrumentDescription = ctrl.instrument.description;
            }
            PageTitleService.setPageTitle(
              'question-management.detail.title', title);
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
