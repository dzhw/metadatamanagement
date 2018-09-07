/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
  function(PageTitleService, $state, ToolbarHeaderService,
    ShoppingCartService, $scope, StudyResource, DataSetSearchService,
    VariableSearchService, DataAcquisitionProjectReleasesResource) {
    PageTitleService.setPageTitle('shopping-cart.title');
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
      name});
    var ctrl = this;
    ctrl.studies = {};
    ctrl.releases = {};

    ctrl.init = function() {
      ctrl.products = ShoppingCartService.getProducts();
      ctrl.products.forEach(function(product) {
        ctrl.studies[product.studyId] = {};
        ctrl.releases[product.projectId] = {};
        DataSetSearchService.countByMultiple({
          'studyId': product.studyId,
          'accessWays': product.accessWay
        }).then(function(result) {
          product.dataSets = result.count;
        });
        VariableSearchService.countByMultiple({
          'studyId': product.studyId,
          'accessWays': product.accessWay
        }).then(function(result) {
          product.variables = result.count;
        });
      });
      _.forEach(ctrl.studies, function(study, studyId) { // jshint ignore:line
        StudyResource.get({id: studyId}).$promise.then(function(study) {
            ctrl.studies[studyId] = study;
          });
      });
      _.forEach(ctrl.releases,
        function(release, projectId) { // jshint ignore:line
        DataAcquisitionProjectReleasesResource.get({id: projectId}).$promise
          .then(function(releases) {
              if (releases.length > 0) {
                ctrl.releases[projectId] = releases[0];
              }
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
