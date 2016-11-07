'use strict';

angular.module('metadatamanagementApp').service('InstrumentBuilderService',
  function(InstrumentResource, CleanJSObjectService) {
    var buildInstrument = function(instrumentFromExcel,
      dataAcquisitionProjectId) {
        var instrument = {
          id: instrumentFromExcel.id,
          dataAcquisitionProjectId: dataAcquisitionProjectId,
          title: {
            en: instrumentFromExcel['title.en'],
            de: instrumentFromExcel['title.de']
          },
          type: instrumentFromExcel.type,
          surveyId: instrumentFromExcel.surveyId
        };
        var cleanedInstrument = CleanJSObjectService
          .removeEmptyJsonObjects(instrument);
        return new InstrumentResource(cleanedInstrument);
      };

    var buildInstrumentAttachmentMetadata = function(metadataFromExcel,
      dataAcquisitionProjectId) {
        var instrumentAttachmentMetadata = {
          instrumentId: metadataFromExcel.instrumentId,
          dataAcquisitionProjectId: dataAcquisitionProjectId,
          title: {
            en: metadataFromExcel['title.en'],
            de: metadataFromExcel['title.de']
          },
          type: {
            en: metadataFromExcel['type.en'],
            de: metadataFromExcel['type.de']
          },
          fileName: metadataFromExcel.filename
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
