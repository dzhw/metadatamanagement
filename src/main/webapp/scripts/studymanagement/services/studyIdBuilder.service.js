/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyIdBuilderService',
function() {
  var buildStudyId = function(dataAcquisitionProjectId) {
    return 'stu-' + dataAcquisitionProjectId + '!';
  };
  return {
    buildStudyId: buildStudyId
  };
});
