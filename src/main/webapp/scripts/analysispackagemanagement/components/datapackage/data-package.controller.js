(function() {
    'use strict';

    function Controller(

    ) {
      var $ctrl = this;

      $ctrl.$onInit = function() {
        $ctrl.package = $ctrl.package || {};
      };
    }

    angular
      .module('metadatamanagementApp')
      .controller('dataPackageController', Controller);
  }

)
();
