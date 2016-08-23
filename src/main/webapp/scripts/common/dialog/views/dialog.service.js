'use strict';

angular.module('metadatamanagementApp').service('DialogService',
  function($mdDialog) {
    var showDialog = function(variables, currentLanguage) {
      var dialogParent = angular.element('#root-container');
      $mdDialog.show({
        controller: 'DialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          variables: variables,
          currentLanguage: currentLanguage
        },
        templateUrl: 'scripts/common/dialog/views/dialog.html.tmpl',
      });
    };
    return {
      showDialog: showDialog
    };
  });
