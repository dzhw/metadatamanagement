(function() {
    'use strict';
  
    var Component = {
      controller: 'LicenseDialogController',
      templateUrl: 'scripts/common/dialogs/license/license-dialog.html.tmpl'
    };
  
    angular
      .module('metadatamanagementApp')
      .component('licenseDialogComponent', Component);
  })();
  