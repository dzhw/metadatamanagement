/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('InstrumentSearchDialogService',
    function($mdDialog) {
      var findInstruments = function(paramObject) {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'InstrumentSearchDialogController',
          controllerAs: 'ctrl',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            paramObject: paramObject
          },
          templateUrl: 'scripts/instrumentmanagement/views/' +
            'instrumentSearchDialog.html.tmpl',
        });
      };
      return {
        findInstruments: findInstruments
      };
    });
