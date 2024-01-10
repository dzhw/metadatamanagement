/* globals _ */
'use strict';

/**
 * A service for displaying license information in a dialog window with markdown support.
 */
angular.module('metadatamanagementApp').service('LicenseDialogService', ['$mdDialog', 
  function($mdDialog) {

    /**
     * Method displaying the dialog with the provided license. The license 
     * needs to be a text and is displayed with markdown support.
     * @param {string} license the text input
     * @param {string} titleTranslateKey the translation key for the dialog title
     * @returns the dialog
     */
    var showLicenseDialog = function(license, titleTranslateKey) {
      var confirm = $mdDialog.confirm({
          controller: 'LicenseDialogController',
          templateUrl: 'scripts/common/dialogs/license/license-dialog.html.tmpl',
          clickOutsideToClose: true,
          locals: {
            content: license,
            titleTranslateKey: titleTranslateKey
          },
          parent: angular.element(document.body)
      })
      
      return $mdDialog.show(confirm);
    }

    return {
      showLicenseDialog: showLicenseDialog
    };
  }])