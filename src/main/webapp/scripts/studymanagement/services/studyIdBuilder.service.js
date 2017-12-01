/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyIdBuilderService',
function() {
  var buildStudyId = function(dataAcquisitionProjectId) {
    return 'stu-' + dataAcquisitionProjectId + '$';
  };

  var buildDoi = function(dataAcquisitionProjectId) {
    return '10.21249/DZHW:' + dataAcquisitionProjectId + ':1.0.0';
  };

  return {
    buildStudyId: buildStudyId,
    buildDoi: buildDoi
  };
});
