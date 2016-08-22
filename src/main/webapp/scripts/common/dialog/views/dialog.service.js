'use strict';

angular.module('metadatamanagementApp').service('DialogService',
  function($mdDialog) {
    var showDialog = function(items, currentLanguage) {
      var dialogParent = angular.element('#root-container');
      $mdDialog.show({
        controller: 'DialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          items: items,
          currentLanguage: currentLanguage
        },
        templateUrl: 'scripts/common/dialog/views/dialog.html.tmpl',
      });
    };
    return {
      showDialog: showDialog
    };
  });
