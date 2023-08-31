/* global document */
(function() {
    'use strict';

    function Controller(
      LanguageService
    ) {
      var $ctrl = this;
      $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      $ctrl.currentPackageIndex = null;
      $ctrl.currentFocusElement = null;
      $ctrl.focus = false;
      $ctrl.packageElement = document.getElementById('packages');
      $ctrl.dataPackageList = {
        dataPackage: {
          type: 'dataPackage',
          en: 'FDZ-DZHW Datapackage',
          de: 'FDZ-DZHW Datenpaket'
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
        $ctrl.packages = $ctrl.packages || [];
        $ctrl.packages.push({
          type: ''
        });
      };

      $ctrl.deletePackage = function(index) {
        $ctrl.packages.splice(index, 1);
        $ctrl.currentForm.$setDirty();
      };

      $ctrl.move = function(dir, index) {
        $ctrl.currentPackageIndex = index;
        var move = {
          up: -1,
          down: +1
        };
        $ctrl.packages
          .splice(index + parseInt(move[dir]), 0,
            $ctrl.packages.splice(index, 1)[0]);
        index = index + parseInt(
          move[dir]
        );
        $ctrl.currentForm.$setDirty();
      };
    }

    angular
      .module('metadatamanagementApp')
      .controller('listDataPackageController', [
      'LanguageService',
      Controller
    ]);
  }

)
();
