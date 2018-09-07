/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
  function(PageTitleService, $state, ToolbarHeaderService,
    ShoppingCartService, $scope, StudyResource) {
    PageTitleService.setPageTitle('shopping-cart.title');
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
      name});
    var ctrl = this;
    ctrl.studies = {};

    ctrl.init = function() {
      ctrl.products = ShoppingCartService.getProducts();
      ctrl.products.forEach(function(product) {
        ctrl.studies[product.studyId] = {};
      });
      _.forEach(ctrl.studies, function(study, studyId) { // jshint ignore:line
        StudyResource.get({id: studyId}).$promise.then(function(study) {
            ctrl.studies[studyId] = study;
          });
      });
    };

    $scope.$on('shopping-cart-changed', function() {
      ctrl.products = ShoppingCartService.getProducts();
    });

    ctrl.deleteProduct = function(product) {
      ShoppingCartService.remove(product);
    };

    ctrl.checkout = function() {
      ShoppingCartService.checkout();
    };

    ctrl.clear = function() {
      ShoppingCartService.clear();
    };

    ctrl.init();
  });
