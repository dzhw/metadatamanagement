'use strict';

angular.module('metadatamanagementApp').service('CurrentDataPackageService', ['$rootScope', 
  function($rootScope) {

    // the currently chosen data package (only public users)
    var currentDataPackage;

    var setCurrentDataPackage = function(dataPackage) {
      if (!angular.equals(currentDataPackage, dataPackage)) {
        currentDataPackage = dataPackage;
        $rootScope.$broadcast('current-data-package-changed',
          currentDataPackage);
      }
    };

    var getCurrentDataPackage = function() {
      return currentDataPackage;
    };

    return {
      setCurrentDataPackage: setCurrentDataPackage,
      getCurrentDataPackage: getCurrentDataPackage
    };
  }]);

