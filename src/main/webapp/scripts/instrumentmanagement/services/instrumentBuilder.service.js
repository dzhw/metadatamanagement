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
    return {
      buildInstrument: buildInstrument
    };
  });
