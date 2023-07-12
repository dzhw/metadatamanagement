/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('DataPackageIdBuilderService',
function() {
  var buildDataPackageId = function(dataAcquisitionProjectId, version) {
    if (version && version !== '') {
      return 'stu-' + dataAcquisitionProjectId + '$-' + version;
    } else {
      return 'stu-' + dataAcquisitionProjectId + '$';
    }
  };

  return {
    buildDataPackageId: buildDataPackageId
  };
});
