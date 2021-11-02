(function() {
    'use strict';

    function Controller(

    ) {
      var $ctrl = this;
      $ctrl.dataPackageList = {};

    }

    angular
      .module('metadatamanagementApp')
      .controller('listDataPackageController', Controller);
  }

)
();
