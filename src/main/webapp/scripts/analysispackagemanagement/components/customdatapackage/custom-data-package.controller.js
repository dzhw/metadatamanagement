(function() {
    'use strict';

    function Controller(
      LanguageService
    ) {
      var $ctrl = this;
      $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      $ctrl.availabilityType = [
        {
          // type: 'ACCESSIBLE',
          de: 'verfügbar',
          en: 'accessible'
        },
        {
          // type: 'NOT_ACCESSIBLE',
          de: 'nicht verfügbar',
          en: 'not accessible'
        }
      ];
      $ctrl.accessWay = ['Download', 'Onsite', 'Remote-Desktop'];
      $ctrl.$onInit = function() {
        $ctrl.package.accessWay = $ctrl.package.accessWay || '';
      };
    }

    angular
      .module('metadatamanagementApp')
      .controller('customDataPackageController', Controller);
  }

)
();
