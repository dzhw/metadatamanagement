'use strict';

angular.module('metadatamanagementApp')
  .service('AnalysisPackageIdBuilderService',
    function() {
      var buildAnalysisPackageId = function(dataAcquisitionProjectId, version) {
        if (version && version !== '') {
          return 'ana-' + dataAcquisitionProjectId + '$-' + version;
        } else {
          return 'ana-' + dataAcquisitionProjectId + '$';
        }
      };

      return {
        buildAnalysisPackageId: buildAnalysisPackageId
      };
    });

// TODO: Create a generic PackageIdBuilder
