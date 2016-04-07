'use strict';

angular.module('metadatamanagementApp').service('AtomicQuestionBuilder',
function(AtomicQuestion, ParserUtil) {
  var getAtomicQuestions = function(atomicQuestions, projectId) {
      var atomicQuestionsObjArray = [];
      for (var i = 0; i < atomicQuestions.length; i++) {
        var data = atomicQuestions[i];
        var atomicQuestionObj = {
            id: data.id,
            name: data.name,
            dataAcquisitionProjectId: projectId,
            questionnaireId: data.questionnaireId,
            variableId: data.variableId,
            footnote: {
              en: data['footnote.en'],
              de: data['footnote.de']
            },
            compositeQuestionName: data.compositeQuestionName,
            instruction: {
              en: data['instruction.en'],
              de: data['instruction.de']
            },
            introduction: {
              en: data['introduction.en'],
              de: data['introduction.de']
            },
            questionText: {
              en: data['questionText.en'],
              de: data['questionText.de']
            },
            sectionHeader: {
              en: data['sectionHeader.en'],
              de: data['sectionHeader.de']
            },
            type: {
              en: data['type.en'],
              de: data['type.de']
            }
          };
        var cleanedAtomicQuestionObject = ParserUtil
          .removeEmptyJsonObjects(atomicQuestionObj);
        atomicQuestionsObjArray[i] = new
        AtomicQuestion(cleanedAtomicQuestionObject);
      }
      return atomicQuestionsObjArray;
    };
  return {
      getAtomicQuestions: getAtomicQuestions
    };
});
