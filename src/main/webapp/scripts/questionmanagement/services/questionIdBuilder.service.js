/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('QuestionIdBuilderService',
  function() {
    var buildQuestionId = function(dataAcquisitionProjectId,
      instrumentNumber, questionNumber) {
      return 'que-' + dataAcquisitionProjectId + '-ins' + instrumentNumber +
        '-' + questionNumber + '!';
    };
    return {
      buildQuestionId: buildQuestionId
    };
  });
