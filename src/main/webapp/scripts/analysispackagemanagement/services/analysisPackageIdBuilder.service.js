'use strict';

angular.module('metadatamanagementApp')
  .service('AnalysisPackageIdBuilderService',
    function() {
      var buildAnalysisPackageId = function(dataAcquisitionProjectId, version) {
        if (version && version !== '') {
          return 'stu-' + dataAcquisitionProjectId + '$-' + version;
        } else {
          return 'stu-' + dataAcquisitionProjectId + '$';
        }
      };

      return {
        buildAnalysisPackageId: buildAnalysisPackageId
      };
    });

// TODO: Create a generic PackageIdBuilder
