/* global html_beautify */
/* global _ */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',
    function(entity, $state, ToolbarHeaderService,
      SimpleMessageToastService, QuestionSearchService, CleanJSObjectService,
      PageTitleService, $rootScope, Principal) {

      var ctrl = this;
      this.representationCodeToggleFlag = true;
      ctrl.predecessors = [];
      ctrl.successors = [];
      ctrl.counts = {};

      entity.promise.then(function(result) {
        var title = {
          questionNumber: result.number,
          questionId: result.id
        };
        if (_.isObject(result.instrument)) {
          title.instrumentDescription = result.instrument.
          description[$rootScope.currentLanguage];
        }
        PageTitleService.
          setPageTitle('question-management.detail.title', title);
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'questionNumber': result.number,
          'instrumentNumber': result.instrumentNumber,
          'instrumentId': result.instrumentId,
          'instrumentIsPresent': CleanJSObjectService.
          isNullOrEmpty(result.instrument) ? false : true,
          'surveys': result.surveys,
          'studyId': result.studyId,
          'studyIsPresent': CleanJSObjectService.
          isNullOrEmpty(result.study) ? false : true,
          'projectId': result.dataAcquisitionProjectId});
        if (result.release || Principal.hasAuthority('ROLE_PUBLISHER')) {
          ctrl.question = result;
          QuestionSearchService.findAllPredeccessors(ctrl.question.id, ['id',
            'instrumentNumber', 'questionText', 'type','instrumentNmber',
            'number', 'dataAcquisitionProjectId', 'instrument.description'],
            0, 100)
          .then(function(predecessors) {
            if (!CleanJSObjectService.isNullOrEmpty(predecessors)) {
              ctrl.predecessors = predecessors.hits.hits;
            }
          });
          if (ctrl.question.successors) {
            QuestionSearchService.findAllSuccessors(ctrl.question.successors,
              ['id', 'instrumentNumber', 'questionText', 'type',
              'instrumentNmber', 'number', 'dataAcquisitionProjectId',
              'instrument.description'], 0, 100)
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
          ctrl.study = result.study;
          ctrl.counts.surveysCount = result.surveys.length;
          if (ctrl.counts.surveysCount === 1) {
            ctrl.survey = result.surveys[0];
          }
          ctrl.counts.variablesCount = result.variables.length;
          if (ctrl.counts.variablesCount === 1) {
            ctrl.variable = result.variables[0];
          }
          ctrl.instrument = result.instrument;
          ctrl.counts.publicationsCount = result.relatedPublications.length;
          if (ctrl.counts.publicationsCount === 1) {
            ctrl.relatedPublication = result.relatedPublications[0];
          }
        } else {
          SimpleMessageToastService.openSimpleMessageToast(
          'question-management.detail.not-released-toast', {id: result.id}
          );
        }
      });
      ctrl.openSuccessCopyToClipboardToast = function(message) {
        SimpleMessageToastService.openSimpleMessageToast(message, []);
      };
      ctrl.toggleRepresentationCode = function() {
        ctrl.representationCodeToggleFlag = !ctrl.representationCodeToggleFlag;
      };
    });
