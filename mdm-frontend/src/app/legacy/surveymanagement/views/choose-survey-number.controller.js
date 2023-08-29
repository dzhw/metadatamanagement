/* global bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ChooseSurveyNumberController',
    function(availableSurveyNumbers, $scope, $mdDialog) {
      $scope.bowser = bowser;
      $scope.availableSurveyNumbers = availableSurveyNumbers;
      $scope.selectedSurveyNumber =
        availableSurveyNumbers[0];

      $scope.closeDialog = function() {
        $mdDialog.hide({
          surveyNumber: $scope.selectedSurveyNumber
        });
      };
    });
