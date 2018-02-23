/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyIdBuilderService',
function(CleanJSObjectService) {
  var buildStudyId = function(dataAcquisitionProjectId) {
    return 'stu-' + dataAcquisitionProjectId + '$';
  };

  var buildDoi = function(dataAcquisitionProject) {
    var version = '0.0.0';
    if (!CleanJSObjectService
      .isNullOrEmpty(dataAcquisitionProject.release.version)) {
      version = dataAcquisitionProject.release.version;
    }
    return '10.21249/DZHW:' + dataAcquisitionProject.id + ':' + version;
  };

  return {
    buildStudyId: buildStudyId,
    buildDoi: buildDoi
  };
});
