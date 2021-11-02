(function() {
    'use strict';

    function Controller(
      LanguageService
    ) {
      var $ctrl = this;
      $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      $ctrl.dataPackageList = {
        dataPackage: {
          type: 'dataPackage',
          en: 'Data Package',
          de: 'Datenpaket'
        },
        externalDataPackage: {
          type: 'externalDataPackage',
          en: 'External Data Package',
          de: 'Externes Datenpaket'
        },
        customDataPackage: {
          type: 'customDataPackage',
          en: 'Custom Data Package',
          de: 'Benutzerdefiniertes Datenpaket'
        }
      };

      $ctrl.$onInit = function() {
        $ctrl.packages = $ctrl.packages || [];
      };

      $ctrl.addPackage = function() {
        $ctrl.packages.push({
          type: 'externalDataPackage'
        });
      };

      $ctrl.deletePackage = function(index) {
        $ctrl.packages.splice(index, 1);
      };
    }

    angular
      .module('metadatamanagementApp')
      .controller('listDataPackageController', Controller);
  }

)
();
