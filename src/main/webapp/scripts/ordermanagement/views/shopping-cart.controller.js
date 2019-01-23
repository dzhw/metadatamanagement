/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
  function(PageTitleService, $state, ToolbarHeaderService,
           ShoppingCartService, $scope, StudyResource, DataSetSearchService,
           VariableSearchService, DataAcquisitionProjectReleasesResource, $q,
           OrderResource, LanguageService, SimpleMessageToastService, order) {

    PageTitleService.setPageTitle('shopping-cart.title');
    ToolbarHeaderService.updateToolbarHeader({
      'stateName': $state.current.name
    });
    var ctrl = this;
    ctrl.studies = {};
    ctrl.releases = {};
    ctrl.counts = {};
    ctrl.initComplete = false;

    ctrl.init = function() {
      var promises = [];
      ctrl.products = ShoppingCartService.getProducts();
      ctrl.products.forEach(function(product) {
        var studyId = product.study.id;

        ctrl.studies[studyId] = {};
        ctrl.releases[product.dataAcquisitionProjectId] = {};
        promises.push(DataSetSearchService.countByMultiple({
          'studyId': studyId,
          'accessWays': product.accessWay
        }).then(function(result) {
          ctrl.counts[studyId + product.accessWay + product.version] =
            ctrl.counts[studyId + product.accessWay +
            product.version] || {};
          ctrl.counts[studyId + product.accessWay +
          product.version].dataSets = result.count;
        }));
        promises.push(VariableSearchService.countByMultiple({
          'studyId': studyId,
          'accessWays': product.accessWay
        }).then(function(result) {
          ctrl.counts[studyId + product.accessWay + product.version] =
            ctrl.counts[studyId + product.accessWay +
            product.version] || {};
          ctrl.counts[studyId + product.accessWay +
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
        // remove all product which are not available anymore
        ctrl.products.forEach(function(product) {
          if (ctrl.studies[product.study.studyId]
            .dataAvailability.en === 'Available') {
            return;
          } else {
            ShoppingCartService.remove(product);
          }
        });
      });
    };

    $scope.$on('shopping-cart-changed', function() {
      ctrl.products = ShoppingCartService.getProducts();
    });

    ctrl.getNumberOfVariables = function(product) {
      return ctrl.counts[product.study.id + product.accessWay + product.version]
        .variables;
    };

    ctrl.getNumberOfDataSets = function(product) {
      return ctrl.counts[product.study.id + product.accessWay + product.version]
        .dataSets;
    };

    ctrl.isCurrentVersion = function(product) {
      return ctrl.releases[product.dataAcquisitionProjectId].version ===
        product.version;
    };

    ctrl.deleteProduct = function(product) {
      ShoppingCartService.remove(product);
    };

    ctrl.order = function() {
      var order = {
        languageKey: LanguageService.getCurrentInstantly(),
        customer: ctrl.customer,
        client: 'MDM',
        products: []
      };
      ctrl.products.forEach(function(product) {
        var completeProduct = {
          dataAcquisitionProjectId: product.dataAcquisitionProjectId,
          study: ctrl.studies[product.study.id],
          accessWay: product.accessWay,
          version: product.version
        };
        order.products.push(completeProduct);
      });
      OrderResource.save(order).$promise.then(function() {
        ShoppingCartService.clear();
        ctrl.orderSaved = true;
      }).catch(function() {
        SimpleMessageToastService.openAlertMessageToast(
          'shopping-cart.toasts.error-on-saving-order');
      });
    };

    ctrl.clear = function() {
      ShoppingCartService.clear();
    };

    if (order) {
      order.$promise.then(function(order) {
        ShoppingCartService
          .initShoppingCartProducts(_.get(order, 'products', []));
        ctrl.init();
      });
    } else {
      ctrl.init();
    }
  });
