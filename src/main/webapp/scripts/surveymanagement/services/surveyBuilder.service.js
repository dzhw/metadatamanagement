'use strict';

angular.module('metadatamanagementApp').service('SurveyBuilderService',
  function(SurveyResource, CleanJSObjectService) {
    var getSurveys = function(surveys, projectId) {
      var surveysObjArray = [];
      for (var i = 0; i < surveys.length; i++) {
        var data = surveys[i];
        console.log(data);
        var surveyObj = {
          id: data.id,
          dataAcquisitionProjectId: projectId,
          instrumentId: data.instrumentId,
          title: {
            en: data['title.en'],
            de: data['title.de']
          },
          fieldPeriod: {
            start: data['fieldPeriod.start'],
            end: data['fieldPeriod.end']
          },
          population: {
            en: data['population.en'],
            de: data['population.de']
          },
          sample: {
            en: data['sample.en'],
            de: data['sample.de']
          },
          surveyMethod: {
            en: data['surveyMethod.en'],
            de: data['surveyMethod.de']
          },
          dataSetIds: CleanJSObjectService.removeWhiteSpace(data.dataSetIds)
        };
        var cleanedSurveyObject = CleanJSObjectService
          .removeEmptyJsonObjects(surveyObj);
        surveysObjArray[i] = new SurveyResource(cleanedSurveyObject);
      }
      return surveysObjArray;
    };
    return {
      getSurveys: getSurveys
    };
  });
