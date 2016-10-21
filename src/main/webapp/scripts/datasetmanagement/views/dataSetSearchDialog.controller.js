'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetSearchDialogController',
    function($mdDialog, $scope, dataSets) {
      var ctlr = this;
      ctlr.dataSets = dataSets;
      ctlr.closeDialog = $mdDialog.hide;
      $scope.$on('$stateChangeStart', function() {
        ctlr.closeDialog();
      });
    });
