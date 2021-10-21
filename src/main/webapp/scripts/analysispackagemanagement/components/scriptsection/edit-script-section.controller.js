(function() {
  'use strict';

  function Controller($scope, uuid) {
    var $ctrl = this;
    var initialState = {
      uuid: uuid.v4(),
      titleDe: '',
      titleEn: '',
      softwarePackage: '',
      softwarePackageVersion: '',
      usedLanguage: ''
    };

    $ctrl.$onInit = function() {
      $ctrl.scripts = $ctrl.scripts || [];
      if ($ctrl.scripts.length === 0) {
        $ctrl.scripts.push(initialState);
      }
      console.log('form ' + $ctrl.currentForm);
    };
    $ctrl.deleteScript = function(index) {
      $ctrl.analysisPackage.additionalScripts.splice(index, 1);
      $scope.analysisPackageForm.$setDirty();
    };
    $ctrl.addScript = function() {
      $ctrl.scripts.push(initialState);
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
