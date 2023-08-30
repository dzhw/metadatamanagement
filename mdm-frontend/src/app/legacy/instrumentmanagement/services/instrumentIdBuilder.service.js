/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('InstrumentIdBuilderService',
  function() {
    var buildInstrumentId = function(dataAcquisitionProjectId,
      instrumentNumber) {
      return 'ins-' + dataAcquisitionProjectId + '-ins' + instrumentNumber +
        '$';
    };
    return {
      buildInstrumentId: buildInstrumentId
    };
  });
