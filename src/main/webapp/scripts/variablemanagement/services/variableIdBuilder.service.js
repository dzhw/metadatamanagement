/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('VariableIdBuilderService',
  function() {
    var buildVariableId = function(dataAcquisitionProjectId, dataSetName,
      variableName) {
      return 'var-' + dataAcquisitionProjectId + '-ds' + dataSetName +
        '-' + variableName + '!';
    };
    return {
      buildVariableId: buildVariableId
    };
  });
