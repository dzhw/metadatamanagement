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
          en: 'Analyzed Data Package',
          de: 'Analysiertes Datenpaket'
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
          type: ''
        });
      };

      $ctrl.deletePackage = function(index) {
        $ctrl.packages.splice(index, 1);
        $ctrl.currentForm.$setDirty();
      };
    }

    angular
      .module('metadatamanagementApp')
      .controller('listDataPackageController', Controller);
  }

)
();
