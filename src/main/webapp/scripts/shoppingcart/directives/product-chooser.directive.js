'use strict';

angular.module('metadatamanagementApp').directive('productChooser',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/shoppingcart/directives/' +
        'product-chooser.html.tmpl',
      scope: {
        projectId: '=',
        study: '=',
        accessWays: '='
      },
      controller: 'ProductChooserController',
      controllerAs: 'ctrl'
    };
  });
