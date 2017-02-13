/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('VariableIdBuilderService',
  function() {
    var buildVariableId = function(dataAcquisitionProjectId, dataSetNumber,
      variableName) {
      return 'var-' + dataAcquisitionProjectId + '-ds' + dataSetNumber +
        '-' + variableName + '!';
    };
    return {
      buildVariableId: buildVariableId
    };
  });
