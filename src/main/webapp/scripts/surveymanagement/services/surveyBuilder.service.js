'use strict';

angular.module('metadatamanagementApp').service('SurveyBuilderService',
  function(SurveyResource, CleanJSObjectService, StudyIdBuilderService,
    SurveyIdBuilderService) {
    var buildSurvey = function(survey, projectId) {
        var data = survey;
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
            title: {
              en: data['population.title.en'],
              de: data['population.title.de']
            },
            description: {
              en: data['population.description.en'],
              de: data['population.description.de']
            }
          },
          sample: {
            en: data['sample.en'],
            de: data['sample.de']
          },
          surveyMethod: {
            en: data['surveyMethod.en'],
            de: data['surveyMethod.de']
          },
          annotations: {
            en: data['annotations.en'],
            de: data['annotations.de']
          },
          dataType: {
            en: data['dataType.en'],
            de: data['dataType.de']
          },
          grossSampleSize: data.grossSampleSize,
          sampleSize: data.sampleSize,
          responseRate: data.responseRate,
          wave: data.wave
        };
        var cleanedSurveyObject = CleanJSObjectService
          .removeEmptyJsonObjects(surveyObj);

        return new SurveyResource(cleanedSurveyObject);
      };
    var buildSurveyAttachmentMetadata = function(metadataFromExcel,
      dataAcquisitionProjectId, excelRowIndex) {
      var surveyAttachmentMetadata = {
        surveyId: SurveyIdBuilderService.buildSurveyId(
          dataAcquisitionProjectId, metadataFromExcel.surveyNumber),
        surveyNumber: metadataFromExcel.surveyNumber,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        description: {
          en: metadataFromExcel['description.en'],
          de: metadataFromExcel['description.de']
        },
        title: metadataFromExcel.title,
        language: metadataFromExcel.language,
        fileName: metadataFromExcel.filename,
        indexInSurvey: excelRowIndex
      };
      var cleanedMetadata = CleanJSObjectService
        .removeEmptyJsonObjects(surveyAttachmentMetadata);
      return cleanedMetadata;
    };
    return {
      buildSurvey: buildSurvey,
      buildSurveyAttachmentMetadata: buildSurveyAttachmentMetadata
    };
  });
