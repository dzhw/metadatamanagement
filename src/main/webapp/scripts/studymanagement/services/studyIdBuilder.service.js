/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyIdBuilderService',
function() {
  var buildStudyId = function(dataAcquisitionProjectId, version) {
    if (version && version !== '') {
      return 'stu-' + dataAcquisitionProjectId + '$-' + version;
    } else {
      return 'stu-' + dataAcquisitionProjectId + '$';
    }
  };

  return {
    buildStudyId: buildStudyId
  };
});
