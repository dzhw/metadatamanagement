(function() {
  'use strict';

  var DetailComponent = {
    controller: [
      '$scope', 
      '$rootScope', 
      'CommonDialogsService', 
      '$mdDialog', 
      'LicenseDialogService', 
      function($scope, $rootScope, CommonDialogsService, $mdDialog, LicenseDialogService) {
      $scope.bowser = $rootScope.bowser;

      /**
       * Method calling the license dialog service to display license info in a dialog window.
       * @param {string} content the license info
       * @param {string} titleTranslateKey the translation key for the dialog title
       */
      $scope.showLicenseInDialog = function(content, titleTranslateKey) {
        LicenseDialogService.showLicenseDialog(content, titleTranslateKey);
      }
    }],
    bindings: {
      lang: '<',
      options: '<',
      collapsed: '<'
    },
    templateUrl: ['$attrs', function($attrs) {
      return $attrs.templateUrl;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzDetail', DetailComponent);
})();
