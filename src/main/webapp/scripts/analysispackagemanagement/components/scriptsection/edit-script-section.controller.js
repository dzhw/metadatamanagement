/* globals _ */
(function() {
  'use strict';

  function Controller(
    uuid,
    isoLanguages,
    LanguageService
  ) {
    var $ctrl = this;
    var isInitialisingSelectedLanguage = false;

    var isoLanguagesArray = Object.keys(isoLanguages).map(function(key) {
      return {
        code: key,
        'displayLanguage': isoLanguages[key]
      };
    });


    // var initSelectedLanguages = function() {
    //   isInitialisingSelectedLanguage = true;
    //     $ctrl['selectedLanguage_'+i] = _.filter(isoLanguagesArray,
    //       function(isoLanguage, i) {
    //         return isoLanguage.code === $ctrl.scripts[i].usedLanguage;
    //       });
    // };

    $ctrl.$onInit = function() {
      $ctrl.scripts = $ctrl.scripts || [];
      $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      // initSelectedLanguages();
    };

    // Script Start
    $ctrl.deleteScript = function(index) {
      $ctrl.scripts.splice(index, 1);
      $ctrl.currentForm.$setDirty();
    };
    $ctrl.addScript = function() {
      $ctrl.scripts.push({
        uuid: uuid.v4(),
        titleDe: '',
        titleEn: '',
        softwarePackage: '',
        softwarePackageVersion: '',
        usedLanguage: ''
      });
      $ctrl.selectedLanguage = '';
    };

    $ctrl.setCurrentScript = function(index, event) {
      $ctrl.currentScriptInputName = event.target.name;
      $ctrl.currentScriptIndex = index;
    };
    $ctrl.moveCurrentScriptUp = function() {};
    $ctrl.moveCurrentScriptDown = function() {};
    // Script stop

    // Languages start
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
        $ctrl.scripts[index].usedLanguage = $ctrl['selectedLanguage_' + index].code;
      } else {
        delete $ctrl.scripts[index].usedLanguage;
      }
      if (!isInitialisingSelectedLanguage) {
        $ctrl.currentForm.$setDirty();
      }
      isInitialisingSelectedLanguage = false;
    };
    // Languages stop
  }

  angular
    .module('metadatamanagementApp')
    .controller('editScriptSectionController', Controller);
})();
