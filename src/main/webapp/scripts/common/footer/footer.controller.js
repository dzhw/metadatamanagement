(function() {
  'use strict';

  function FooterController(
    $scope,
    $rootScope,
    FdzFeedbackDialogService
  ) {
    var $ctrl = this;
    $ctrl.openDialog = function() {
      var openByNavbarFeedbackButton = true;
      FdzFeedbackDialogService.showDialog(openByNavbarFeedbackButton);
    };
    $scope.$watch(function() {
        return $rootScope.currentLanguage;
      },
      function(newVal, oldVal) {
        if (newVal !== oldVal) {
          $ctrl.lang = $rootScope.currentLanguage;
        }
      });
  }

  angular
    .module('metadatamanagementApp')
    .controller('footerController', FooterController);
})();
