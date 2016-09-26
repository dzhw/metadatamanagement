'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveySearchDialogController',
  function($mdDialog, $scope, surveys) {
    $scope.surveys = surveys;
    $scope.closeDialog = $mdDialog.hide;
    $scope.$on('$stateChangeStart', function() {
      $scope.closeDialog();
    });
  });
