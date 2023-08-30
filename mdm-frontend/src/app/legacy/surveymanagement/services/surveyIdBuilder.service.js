/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SurveyIdBuilderService',
  function() {
    var buildSurveyId = function(dataAcquisitionProjectId, surveyNumber) {
      return 'sur-' + dataAcquisitionProjectId + '-sy' + surveyNumber +
        '$';
    };
    return {
      buildSurveyId: buildSurveyId
    };
  });
