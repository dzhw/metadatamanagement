'use strict';

angular.module('metadatamanagementApp').service('MigrationService',
  function(VERSION, localStorageService, ShoppingCartService) {

    var versionKey = 'version';

    var migrate = function() {
      var storedVersion = localStorageService.get(versionKey);
      if (storedVersion !== VERSION) {
        ShoppingCartService.clearProducts();
        localStorageService.set(versionKey, VERSION);
      }
    };

    return {
      migrate: migrate
    };
  });
