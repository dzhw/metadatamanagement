'use strict';

angular.module('metadatamanagementApp').service('SurveyBuilderService',
function(Survey, CleanJSObjectService) {
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
            dataSetIds: CleanJSObjectService.removeWhiteSpace(data.dataSetIds)
          };
        var cleanedSurveyObject = CleanJSObjectService
              .removeEmptyJsonObjects(surveyObj);
        surveysObjArray[i] = new Survey(cleanedSurveyObject);
      }
      return surveysObjArray;
    };
  return {
      getSurveys: getSurveys
    };
});
