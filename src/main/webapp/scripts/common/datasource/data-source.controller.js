/* global $, document */

(function() {
  'use strict';

  function Controller(
    $timeout,
    $document,
    LanguageService
  ) {
    var $ctrl = this;

    $ctrl.currentLanguage = LanguageService.getCurrentInstantly();

    $ctrl.$onInit = function() {
      $ctrl.content = $ctrl.content || [];
    };

    $ctrl.deleteLink = function(index) {
      $ctrl.content.splice(index, 1);
      $ctrl.currentForm.$setDirty();
    };

    $ctrl.addLink = function() {
      if (!$ctrl.content) {
        $ctrl.content = [];
      }
      $ctrl.content.push({
        url: '',
        name: {
          de: '',
          en: ''
        }
      });
      $timeout(function() {
        $document.find('input[name="' + $ctrl.name + 'DisplayTextDe' +
          $ctrl.index + '_' + ($ctrl.content.length - 1) + '"]')
          .focus();
      }, 200);
    };

    $ctrl.setCurrentLink = function(index, event) {
      $ctrl.currentLinkInputName = event.target.name;
      $ctrl.currentLinkIndex = index;
    };

    $ctrl.deleteCurrentLink = function(index, event) {
      if (document.activeElement &&
        $(document.activeElement).parents('#dataSource-' + index)
          .length) {
        return;
      }
      if (event.relatedTarget && (
        event.relatedTarget.id === 'move-link-up-button' ||
        event.relatedTarget.id === 'move-link-down-button')) {
        return;
      }
      delete $ctrl.currentLinkIndex;
    };

    $ctrl.moveCurrentLinkUp = function() {
      var a = $ctrl.content[$ctrl.currentLinkIndex - 1];
      $ctrl.content[$ctrl.currentLinkIndex - 1] =
        $ctrl.content[$ctrl.currentLinkIndex];
      $ctrl.content[$ctrl.currentLinkIndex] = a;
      $ctrl.currentLinkInputName = $ctrl.currentLinkInputName
        .replace('_' + $ctrl.currentLinkIndex,
          '_' + ($ctrl.currentLinkIndex - 1));
      $document.find('input[name="' + $ctrl.currentLinkInputName + '"]')
        .focus();
      $ctrl.currentForm.$setDirty();
    };

    $ctrl.moveCurrentLinkDown = function() {
      var a = $ctrl.content[$ctrl.currentLinkIndex + 1];
      $ctrl.content[$ctrl.currentLinkIndex + 1] =
        $ctrl.content[$ctrl.currentLinkIndex];
      $ctrl.content[$ctrl.currentLinkIndex] = a;
      $ctrl.currentLinkInputName = $ctrl.currentLinkInputName
        .replace('_' + $ctrl.currentLinkIndex,
          '_' + ($ctrl.currentLinkIndex + 1));
      $document.find('input[name="' + $ctrl.currentLinkInputName + '"]')
        .focus();
      $ctrl.currentForm.$setDirty();
    };

  }

  angular
    .module('metadatamanagementApp')
    .controller('dataSourceController', Controller);
})();
