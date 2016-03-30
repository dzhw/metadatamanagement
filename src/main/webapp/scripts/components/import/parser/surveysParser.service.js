'use strict';

angular.module('metadatamanagementApp').service('SurveysParser',
function(Survey, ArrayParser) {
  var getSurveys = function(surveys, projectId) {
      var surveysObjArray = [];
      for (var i = 0; i < surveys.length; i++) {
        var data = surveys[i];
        var surveyObj = {
            id: data.id,
            dataAcquisitionProjectId: projectId,
            questionnaireId: data.questionnaireId,
            title: {
              en: data['title.en'],
              de: data['title.de']
            },
            fieldPeriod: {
              start: data['fieldPeriod.start'],
              end: data['fieldPeriod.end']
            },
            dataSetIds: ArrayParser.getParsedArray(data.dataSetIds)
          };
        surveysObjArray[i] = new Survey(surveyObj);
      }
      return surveysObjArray;
    };
  return {
      getSurveys: getSurveys
    };
});
