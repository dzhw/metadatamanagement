'use strict';

angular.module('metadatamanagementApp').service('SurveyBuilderService',
  function(SurveyResource, CleanJSObjectService, StudyIdBuilderService,
    SurveyIdBuilderService) {
    var getSurveys = function(surveys, projectId) {
      var surveysObjArray = [];
      for (var i = 0; i < surveys.length; i++) {
        var data = surveys[i];
        var surveyObj = {
          id: SurveyIdBuilderService.buildSurveyId(projectId, data.number),
          number: data.number,
          dataAcquisitionProjectId: projectId,
          studyId: StudyIdBuilderService.buildStudyId(projectId),
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
          grossSampleSize: data.grossSampleSize,
          sampleSize: data.sampleSize,
          responseRate: data.responseRate
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
