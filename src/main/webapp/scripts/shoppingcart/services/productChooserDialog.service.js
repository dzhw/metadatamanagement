'use strict';

angular.module('metadatamanagementApp').service('ProductChooserDialogService',
  function($mdDialog) {
    var showDialog = function(projectId, accessWays, study, event) {
      $mdDialog.show({
        templateUrl: 'scripts/shoppingcart/views/' +
        'product-chooser.html.tmpl',
        controller: 'ProductChooserController',
        controllerAs: 'ctrl',
        targetEvent: event,
        locals: {
          study: study,
          accessWays: accessWays || [],
          projectId: projectId
        },
        clickOutsideToClose: false,
        closeTo: '#shoppingCartButton',
        fullscreen: true
      });
    };
    return {
      showDialog: showDialog
    };
  });
