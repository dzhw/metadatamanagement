(function() {
  'use strict';

  function Controller() {
    var $ctrl = this;
    $ctrl.controlName = '';

    $ctrl.upperCase = function(string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
    };

    $ctrl.$onInit = function() {
      if ($ctrl.name) {
        $ctrl.controlName = $ctrl.name + $ctrl
          .upperCase($ctrl.translationKeyName);
      } else {
        $ctrl.controlName = $ctrl.translationKeyName;
      }
    };
  }

  angular
    .module('metadatamanagementApp')
    .controller('textController', Controller);
})();
