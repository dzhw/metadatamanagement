'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionSearchDialogController',
    function($mdDialog, $scope, questions) {
      var ctlr = this;
      ctlr.questions = questions;
      ctlr.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        ctlr.closeDialog();
      });
    });
