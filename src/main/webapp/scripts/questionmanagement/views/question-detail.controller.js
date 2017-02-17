/* global html_beautify */
/* global _ */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',
    function(StudySearchService, entity,
      SimpleMessageToastService, QuestionSearchService, CleanJSObjectService,
      VariableSearchService, RelatedPublicationSearchService,
      InstrumentSearchService, PageTitleService, $rootScope) {

      var ctrl = this;
      this.representationCodeToggleFlag = true;
      ctrl.question = entity;
      ctrl.predecessors = [];
      ctrl.successors = [];
      ctrl.counts = {};

      entity.$promise.then(function() {
        console.log(ctrl.question);
        ctrl.questionIdAsArray = ctrl.question.id.split(',');
        QuestionSearchService.findAllPredeccessors(ctrl.question.id, ['id',
        'instrumentNumber', 'questionText', 'type','instrumentNmber',
        'number', 'dataAcquisitionProjectId', 'instrument.description'])
          .then(function(predecessors) {
            if (!CleanJSObjectService.isNullOrEmpty(predecessors)) {
              ctrl.predecessors = predecessors.hits.hits;
            }
          });
        if (ctrl.question.successors) {
          QuestionSearchService.findSuccessors(ctrl.question.successors, ['id',
          'instrumentNumber', 'questionText', 'type','instrumentNmber',
          'number', 'dataAcquisitionProjectId', 'instrument.description'])
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
        StudySearchService.findOneByProjectId(ctrl.question.
          dataAcquisitionProjectId, ['dataAcquisitionProjectId','title', 'id'])
          .then(function(study) {
            if (study.hits.hits.length > 0) {
              ctrl.study = study.hits.hits[0]._source;
            }
          });
        VariableSearchService.countBy('relatedQuestions.questionId',
          ctrl.question.id).then(function(variablesCount) {
          ctrl.counts.variablesCount = variablesCount.count;
          if (variablesCount.count === 1) {
            VariableSearchService
              .findByQuestionId(ctrl.question.id, ['dataAcquisitionProjectId',
              'dataSetNumber', 'name', 'label'])
              .then(function(variable) {
                ctrl.variable = variable.hits.hits[0]._source;
              });
          }
        });
        InstrumentSearchService.findInstruments(ctrl.question.instrumentId,
          ['dataAcquisitionProjectId', 'number', 'title', 'description'])
          .then(function(instrument) {
                  var title = {
                    questionNumber: ctrl.question.number,
                    questionId: ctrl.question.id
                  };
                  if (instrument.docs[0].found === true) {
                    ctrl.instrument = instrument.docs[0]._source;
                    title.instrumentDescription = ctrl.instrument
                    .description[$rootScope.currentLanguage];
                  }
                  PageTitleService.
                  setPageTitle('question-management.detail.title', title);
                });
        RelatedPublicationSearchService.countBy('questionIds',
        ctrl.question.id).then(function(publicationsCount) {
                  ctrl.counts.publicationsCount = publicationsCount.count;
                  if (publicationsCount.count === 1) {
                    RelatedPublicationSearchService
                      .findByQuestionId(ctrl.question.id, ['id', 'title'])
                      .then(function(relatedPublication) {
                        ctrl.relatedPublication = relatedPublication.
                        hits.hits[0]._source;
                      });
                  }
                });
      });
      ctrl.openSuccessCopyToClipboardToast = function(message) {
        SimpleMessageToastService.openSimpleMessageToast(message, []);
      };
      ctrl.toggleRepresentationCode = function() {
        ctrl.representationCodeToggleFlag = !ctrl.representationCodeToggleFlag;
      };
    });
