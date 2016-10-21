'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationSearchDialogController',
    function($mdDialog, $scope, relatedPublications) {
      var ctlr = this;
      ctlr.relatedPublications = relatedPublications;
      ctlr.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        ctlr.closeDialog();
      });
    });
