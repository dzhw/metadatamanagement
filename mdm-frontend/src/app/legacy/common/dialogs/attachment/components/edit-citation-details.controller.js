'use strict';

angular.module('metadatamanagementApp')
  .controller('EditCitationDetailsController', [
  '$scope',
  '$rootScope',
    function($scope, $rootScope) {
      var $ctrl = this;
      $ctrl.currentYear = new Date().getFullYear();
      $scope.bowser = $rootScope.bowser;
      this.$onInit = function() {
        $ctrl.citationDetails = $ctrl.citationDetails || {};

        $scope.isQuestionnaire = $ctrl.attachmentMetadataType === "Questionnaire"
          || $ctrl.attachmentMetadataType === "Variable Questionnaire";

        if (!$ctrl.citationDetails.publicationYear && !$scope.isQuestionnaire) {
          $ctrl.citationDetails.publicationYear = $ctrl.currentYear;
        }
        $scope.form = $ctrl.currentForm;
      };

      // For questionnaire types: Show error for questionnaire mandatory fields publicationYear,
      // location and institution if user entered only some of them. 
      // All of them together have to be given or have to be empty.
      $scope.questionnaireFieldsInvalid = function() {
        return !((!$ctrl.citationDetails.institution || !$ctrl.citationDetails.location || !$ctrl.citationDetails.publicationYear) && 
          (!$ctrl.citationDetails.institution && !$ctrl.citationDetails.location && !$ctrl.citationDetails.publicationYear));
      }
    }]);

