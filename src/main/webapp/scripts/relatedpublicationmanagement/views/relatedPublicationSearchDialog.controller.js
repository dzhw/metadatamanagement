'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationSearchDialogController',
    function($mdDialog, $scope, methodName, methodParams, count) {
      var RelatedPublicationSearchDialogController = this;
      RelatedPublicationSearchDialogController.methodName = methodName;
      RelatedPublicationSearchDialogController.methodParams = methodParams;
      RelatedPublicationSearchDialogController.count = count;
      RelatedPublicationSearchDialogController.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        RelatedPublicationSearchDialogController.closeDialog();
      });
    });
