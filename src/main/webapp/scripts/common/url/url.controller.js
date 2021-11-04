(function() {
  'use strict';

  function Controller(LanguageService) {
    var $ctrl = this;
    $ctrl.currentLanguage = LanguageService.getCurrentInstantly();

    $ctrl.$onInit = function() {
      $ctrl.content.dataSources = $ctrl.content.dataSources || [];
    };

    $ctrl.deleteLink = function(index) {
      if (!index) {
        $ctrl.content.dataSources[0].url = '';
        $ctrl.content.dataSources[0].displayText.en = '';
        $ctrl.content.dataSources[0].displayText.de = '';
      }
    };

  }

  angular
    .module('metadatamanagementApp')
    .controller('urlController', Controller);
})();
