'use strict';

angular.module('metadatamanagementApp').service('InstrumentBuilderService',
  function(InstrumentResource, CleanJSObjectService) {
    var buildInstrument = function(instrumentFromExcel,
      dataAcquisitionProjectId) {
        console.log(instrumentFromExcel);
        console.log(dataAcquisitionProjectId);
        console.log(InstrumentResource);
        console.log(CleanJSObjectService);
      };
    return {
      buildInstrument: buildInstrument
    };
  });
