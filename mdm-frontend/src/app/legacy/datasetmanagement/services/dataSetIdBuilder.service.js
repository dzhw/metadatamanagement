/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('DataSetIdBuilderService',
  function() {
    var buildDataSetId = function(dataAcquisitionProjectId, dataSetNumber) {
      return 'dat-' + dataAcquisitionProjectId + '-ds' + dataSetNumber +
        '$';
    };
    return {
      buildDataSetId: buildDataSetId
    };
  });
