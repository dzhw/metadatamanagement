'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionSearchDialogController',
  function($mdDialog, $scope, questions) {
    $scope.questions = questions;
    $scope.closeDialog = $mdDialog.hide;
    $scope.$on('$stateChangeStart', function() {
      $scope.closeDialog();
    });
  });
