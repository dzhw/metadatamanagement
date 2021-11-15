/* global document */
(function() {
    'use strict';

    function Controller(
      $timeout,
      $scope,
      LanguageService
    ) {
      var $ctrl = this;
      var formElements = ['INPUT', 'SELECT', 'TEXTAREA'];
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

      $ctrl.move = function(dir) {
        var move = {
          up: -1,
          down: +1
        };
        $ctrl.packages
          .splice($ctrl.currentPackageIndex + parseInt(move[dir]), 0,
            $ctrl.packages.splice($ctrl.currentPackageIndex, 1)[0]);
        $ctrl.currentPackageIndex = $ctrl.currentPackageIndex + parseInt(
          move[dir]
        );
        $timeout(function() {
          document.getElementsByName(
            angular.element($ctrl.currentFocusElement)
              .attr('name').split('_')[0] + '_' + $ctrl.currentPackageIndex)[0]
            .focus();
        }, 1000);

        $ctrl.currentForm.$setDirty();
      };

      $scope.$watch(function() {
        return document.activeElement;
      }, function(value) {
        if ($ctrl.packageElement.contains(value) && formElements
          .indexOf(value.tagName) > -1) {
          console.log('package: ' + value.tagName);
          var dataSources = document.getElementById('dataSources');
          if (!dataSources.contains(value)) {
            $ctrl.focus = true;
            $ctrl.currentFocusElement = value;
            $ctrl.currentPackageIndex = parseInt(
              angular.element(value).attr('name').slice(-1)
            );
          }
        } else {
          $ctrl.focus = false;
        }
      });
    }

    angular
      .module('metadatamanagementApp')
      .controller('listDataPackageController', Controller);
  }

)
();
