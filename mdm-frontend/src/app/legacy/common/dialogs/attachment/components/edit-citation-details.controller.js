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
        if (!$ctrl.citationDetails.publicationYear) {
          $ctrl.citationDetails.publicationYear = $ctrl.currentYear;
        }
        $scope.form = $ctrl.currentForm;
      };
    }]);

