(function() {
  'use strict';

  function Controller() {
    var $ctrl = this;
    $ctrl.upperCase = function(string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
    };

    $ctrl.$onInit = function() {
      if($ctrl.name) {
        $ctrl.translationKeyName = $ctrl.upperCase($ctrl.translationKeyName);
      }
    };
  }

  angular
    .module('metadatamanagementApp')
    .controller('textController', Controller);
})();
