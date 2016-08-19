'use strict';

angular.module('metadatamanagementApp').service('DialogService',
  function($mdDialog) {
    var showDialog = function(items) {
      var dialogParent = angular.element('#content');
      $mdDialog.show({
        controller: 'DialogController',
        parent: dialogParent,
        clickOutsideToClose: true,
        locals: {
          items: items
        },
        templateUrl: 'scripts/common/dialog/views/dialog.html.tmpl',
      });
    };
    return {
      showDialog: showDialog
    };
  });
