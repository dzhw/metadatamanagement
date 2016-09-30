'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySearchDialogController',
    function($mdDialog, $scope, surveys) {
      var ctlr = this;
      ctlr.surveys = surveys;
      ctlr.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        ctlr.closeDialog();
      });
    });
