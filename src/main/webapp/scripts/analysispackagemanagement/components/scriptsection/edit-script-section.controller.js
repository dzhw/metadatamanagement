/* globals _ */
(function() {
  'use strict';

  function Controller(
    uuid,
    isoLanguages,
    LanguageService,
    ScriptSoftwarePackagesResource
  ) {
    var $ctrl = this;
    var isInitialisingSelectedLanguage = false;
    var isInitialisingSelectedSoftwarePackage = false;
    var isoLanguagesArray = Object.keys(isoLanguages).map(function(key) {
      return {
        code: key,
        'displayLanguage': isoLanguages[key]
      };
    });
    var initSelectedLanguages = function() {
      isInitialisingSelectedLanguage = true;
      _.forEach($ctrl.scripts, function(value, index) {
        var result = _.filter(isoLanguagesArray,
          function(isoLanguage) {
            return isoLanguage.code === value.usedLanguage;
          });
        $ctrl['selectedLanguage_' + index] = result[0]
          .displayLanguage[$ctrl.currentLanguage];
      });
    };

    var softwarePackages = [];
    var getSoftwarePackages = function() {
      ScriptSoftwarePackagesResource.get()
        .$promise.then(function(softwarePackage) {
          angular.copy(softwarePackage, softwarePackages);
        });
    };
    var initSelectedSoftwarePackages = function() {
      _.forEach($ctrl.scripts, function(value, index) {
        $ctrl['selectedSoftwarePackage_' + index] = value.softwarePackage;
      });
    };

    $ctrl.$onInit = function() {
      $ctrl.scripts = $ctrl.scripts || [];
      $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      initSelectedLanguages();
      getSoftwarePackages();
      initSelectedSoftwarePackages();
    };

    $ctrl.deleteScript = function(index) {
      $ctrl.scripts.splice(index, 1);
      $ctrl['languageSearchText_' + index] = '';
      $ctrl['selectedLanguage_' + index] = null;
      $ctrl['softwarePackageSearchText_' + index] = '';
      $ctrl['selectedSoftwarePackage_' + index] = null;
      $ctrl.currentForm.$setDirty();
    };

    $ctrl.addScript = function() {
      $ctrl.scripts.push({
        uuid: uuid.v4(),
        title: {
          de: '',
          en: ''
        },
        softwarePackage: '',
        softwarePackageVersion: '',
        usedLanguage: ''
      });
    };

    $ctrl.setCurrentScript = function(index, event) {
      $ctrl.currentScriptInputName = event.target.name;
      $ctrl.currentScriptIndex = index;
    };
    $ctrl.moveCurrentScriptUp = function() {};
    $ctrl.moveCurrentScriptDown = function() {};

    $ctrl.searchLanguages = function(searchText) {
      if (!searchText || searchText === '') {
        return isoLanguagesArray;
      }
      var lowerCasedSearchText = searchText.toLowerCase();
      return _.filter(isoLanguagesArray, function(isoLanguage) {
        return isoLanguage.displayLanguage[$ctrl.currentLanguage]
          .toLowerCase().indexOf(lowerCasedSearchText) > -1;
      });
    };

    $ctrl.selectedLanguageChanged = function(index) {
      if ($ctrl['selectedLanguage_' + index]) {
        $ctrl.scripts[index]
          .usedLanguage = $ctrl['selectedLanguage_' + index].code;
      } else {
        delete $ctrl.scripts[index].usedLanguage;
      }
      if (!isInitialisingSelectedLanguage) {
        $ctrl.currentForm.$setDirty();
      }
      isInitialisingSelectedLanguage = false;
    };

    $ctrl.searchSoftwarePackages = function(searchText) {
      if (!searchText || searchText === '') {
        return softwarePackages;
      }
      var lowerCasedSearchText = searchText.toLowerCase();
      return _.filter(softwarePackages, function(softwarePackage) {
        return softwarePackage.toLowerCase().indexOf(lowerCasedSearchText) > -1;
      });
    };

    $ctrl.selectedSoftwarePackageChanged = function(index) {
      if ($ctrl['selectedSoftwarePackage_' + index]) {
        $ctrl.scripts[index].softwarePackage =
          $ctrl['selectedSoftwarePackage_' + index];
      } else {
        delete $ctrl.scripts[index].softwarePackage;
      }
      if (!isInitialisingSelectedLanguage) {
        $ctrl.currentForm.$setDirty();
      }
      isInitialisingSelectedSoftwarePackage = false;
    };
  }

  angular
    .module('metadatamanagementApp')
    .controller('editScriptSectionController', Controller);
})();
