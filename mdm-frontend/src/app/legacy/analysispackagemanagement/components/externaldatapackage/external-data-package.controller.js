(function() {
    'use strict';

    function Controller(
      LanguageService
    ) {
      var $ctrl = this;
      $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      $ctrl.availabilityType = [
        {
          // type: 'OPEN_ACCESS',
          de: 'Open Access',
          en: 'open-access'
        },
        {
          // type: 'RESTRICTED_ACCESS',
          de: 'beschränkter Zugang',
          en: 'restricted access'
        },
        {
          // type: 'NOT_ACCESSIBLE',
          de: 'nicht zugänglich',
          en: 'not accessible'
        }
      ];
      $ctrl.$onInit = function() {
        $ctrl.package.availabilityType = $ctrl.package.availabilityType || {};
      };
    }

    angular
      .module('metadatamanagementApp')
      .controller('externalDataPackageController', [
      'LanguageService',
      Controller
    ]);
  }

)
();
