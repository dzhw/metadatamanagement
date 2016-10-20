'use strict';

angular.module('metadatamanagementApp')
  .controller('StudySearchDialogController',
    function($mdDialog, $scope, studies) {
      var ctlr = this;
      ctlr.studies = studies;
      ctlr.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        ctlr.closeDialog();
      });
    });
