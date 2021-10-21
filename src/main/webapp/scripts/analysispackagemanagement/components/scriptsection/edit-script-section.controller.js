(function() {
  'use strict';

  function Controller($scope) {
    var $ctrl = this;
    $ctrl.deleteScript = function(index) {
      $ctrl.analysisPackage.additionalScripts.splice(index, 1);
      $scope.analysisPackageForm.$setDirty();
    };
    $ctrl.addScript = function() {
      if (!$ctrl.analysisPackage.additionalScripts) {
        $ctrl.analysisPackage.additionalScripts = [];
      }
    };
    $ctrl.setCurrentScript = function(index, event) {
      $ctrl.currentScriptInputName = event.target.name;
      $ctrl.currentScriptIndex = index;
    };
    $ctrl.deleteCurrentLink = function() {

    };
    $ctrl.moveCurrentScriptUp = function() {};
    $ctrl.moveCurrentScriptDown = function() {};

  }

  angular
    .module('metadatamanagementApp')
    .controller('editScriptSectionController', Controller);
})();
