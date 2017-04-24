/* global _*/
'use strict';

angular.module('metadatamanagementApp').service('InstrumentBuilderService',
  function(InstrumentResource, CleanJSObjectService,
    InstrumentIdBuilderService, SurveyIdBuilderService, StudyIdBuilderService) {
    var buildInstrument = function(instrumentFromExcel,
      dataAcquisitionProjectId) {
      var instrument = {
        id: InstrumentIdBuilderService.buildInstrumentId(
          dataAcquisitionProjectId, instrumentFromExcel.number),
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        studyId: StudyIdBuilderService.buildStudyId(dataAcquisitionProjectId),
        number: instrumentFromExcel.number,
        title: {
          en: instrumentFromExcel['title.en'],
          de: instrumentFromExcel['title.de']
        },
        description: {
          en: instrumentFromExcel['description.en'],
          de: instrumentFromExcel['description.de']
        },
        type: instrumentFromExcel.type,
        surveyNumbers: (instrumentFromExcel.surveyNumbers + '').split(
          ','),
        surveyIds: []
      };
      _.forEach(instrument.surveyNumbers, function(number) {
        instrument.surveyIds
          .push(SurveyIdBuilderService.buildSurveyId(
            instrument.dataAcquisitionProjectId, number.trim()));
      });
      var cleanedInstrument = CleanJSObjectService
        .removeEmptyJsonObjects(instrument);
      return new InstrumentResource(cleanedInstrument);
    };

    var buildInstrumentAttachmentMetadata = function(metadataFromExcel,
      dataAcquisitionProjectId, excelRowIndex) {
      var instrumentAttachmentMetadata = {
        instrumentId: InstrumentIdBuilderService.buildInstrumentId(
          dataAcquisitionProjectId, metadataFromExcel.instrumentNumber),
        instrumentNumber: metadataFromExcel.instrumentNumber,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        description: {
          en: metadataFromExcel['description.en'],
          de: metadataFromExcel['description.de']
        },
        type: {
          en: metadataFromExcel['type.en'],
          de: metadataFromExcel['type.de']
        },
        title: metadataFromExcel.title,
        language: metadataFromExcel.language,
        fileName: metadataFromExcel.filename,
        indexInInstrument: excelRowIndex
      };
      var cleanedMetadata = CleanJSObjectService
        .removeEmptyJsonObjects(instrumentAttachmentMetadata);
      return cleanedMetadata;
    };
    return {
      buildInstrument: buildInstrument,
      buildInstrumentAttachmentMetadata: buildInstrumentAttachmentMetadata
    };
  });
