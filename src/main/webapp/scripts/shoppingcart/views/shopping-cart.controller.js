/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
  function(PageTitleService, $state, ToolbarHeaderService,
    ShoppingCartService, $scope, StudyResource, DataSetSearchService,
    VariableSearchService, DataAcquisitionProjectReleasesResource, $q) {
    PageTitleService.setPageTitle('shopping-cart.title');
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
      name});
    var ctrl = this;
    ctrl.studies = {};
    ctrl.releases = {};
    ctrl.counts = {};
    ctrl.initComplete = false;

    ctrl.init = function() {
      var promises = [];
      ctrl.products = ShoppingCartService.getProducts();
      ctrl.products.forEach(function(product) {
        ctrl.studies[product.studyId] = {};
        ctrl.releases[product.projectId] = {};
        promises.push(DataSetSearchService.countByMultiple({
          'studyId': product.studyId,
          'accessWays': product.accessWay
        }).then(function(result) {
          ctrl.counts[product.studyId + product.accessWay + product.version] =
            ctrl.counts[product.studyId + product.accessWay +
              product.version] || {};
          ctrl.counts[product.studyId + product.accessWay +
            product.version].dataSets = result.count;
        }));
        promises.push(VariableSearchService.countByMultiple({
          'studyId': product.studyId,
          'accessWays': product.accessWay
        }).then(function(result) {
          ctrl.counts[product.studyId + product.accessWay + product.version] =
            ctrl.counts[product.studyId + product.accessWay +
              product.version] || {};
          ctrl.counts[product.studyId + product.accessWay +
            product.version].variables = result.count;
        }));
      });
      _.forEach(ctrl.studies, function(study, studyId) { // jshint ignore:line
        promises.push(StudyResource.get({id: studyId}).$promise.then(
          function(study) {
            ctrl.studies[studyId] = study;
          }));
      });
      _.forEach(ctrl.releases,
        function(release, projectId) { // jshint ignore:line
        promises.push(DataAcquisitionProjectReleasesResource.get(
          {id: projectId}).$promise.then(function(releases) {
              if (releases.length > 0) {
                ctrl.releases[projectId] = releases[0];
              }
            }));
      });
      $q.all(promises).then(function() {
        ctrl.initComplete = true;
      });
    };

    $scope.$on('shopping-cart-changed', function() {
      ctrl.products = ShoppingCartService.getProducts();
    });

    ctrl.getNumberOfVariables = function(product) {
      return ctrl.counts[product.studyId + product.accessWay + product.version]
        .variables;
    };

    ctrl.getNumberOfDataSets = function(product) {
      return ctrl.counts[product.studyId + product.accessWay + product.version]
        .dataSets;
    };

    ctrl.isCurrentVersion = function(product) {
      return ctrl.releases[product.projectId].version === product.version;
    };

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
