'use strict';

angular.module('metadatamanagementApp').service('CurrentAnalysisPackageService', ['$rootScope', 
  function($rootScope) {

    // the currently chosen analysis package (only public users)
    var currentAnalysisPackage;

    var setCurrentAnalysisPackage = function(analysisPackage) {
      if (!angular.equals(currentAnalysisPackage, analysisPackage)) {
        currentAnalysisPackage = analysisPackage;
        $rootScope.$broadcast('current-analysis-package-changed',
          currentAnalysisPackage);
      }
    };

    var getCurrentAnalysisPackage = function() {
      return currentAnalysisPackage;
    };

    return {
      setCurrentAnalysisPackage: setCurrentAnalysisPackage,
      getCurrentAnalysisPackage: getCurrentAnalysisPackage
    };
  }]);

