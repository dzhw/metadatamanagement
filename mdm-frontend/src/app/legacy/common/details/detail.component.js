(function() {
  'use strict';

  var DetailComponent = {
    controller: ['$scope', '$rootScope', 'CommonDialogsService', function($scope, $rootScope, CommonDialogsService) {
      $scope.bowser = $rootScope.bowser;

      /**
       * Shows license info in dialog window.
       * @param {*} content the license info
       */
      $scope.showLicenseInDialog = function(content) {
        CommonDialogsService.showInfoDialog(
          'global.common-dialogs' +
          '.info.license',
          {},
          content,
          null
        );
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
