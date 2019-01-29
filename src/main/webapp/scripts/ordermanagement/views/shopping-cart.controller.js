/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
  function(PageTitleService, $state, ToolbarHeaderService,
    ShoppingCartService, $scope, StudyResource, DataSetSearchService,
    VariableSearchService, DataAcquisitionProjectReleasesResource, $q,
    OrderResource, LanguageService, SimpleMessageToastService,
    DataAcquisitionProjectResource) {

    PageTitleService.setPageTitle('shopping-cart.title');
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
      name});
    var ctrl = this;
    ctrl.dataAcquisitionProjects = {};
    ctrl.studies = {};
    ctrl.releases = {};
    ctrl.customer = {};
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
        var project = DataAcquisitionProjectResource
          .get({id: product.projectId});

        ctrl.dataAcquisitionProjects[product.projectId] = project;
        promises.push(project.$promise);
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
          if (ctrl.studies[product.studyId]
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

    ctrl.order = function() {
      if ($scope.customerForm.$valid) {
        var order = {
          languageKey: LanguageService.getCurrentInstantly(),
          customer: ctrl.customer,
          client: 'MDM',
          products: []
        };
        ctrl.products.forEach(function(product) {
          var completeProduct = {
            dataAcquisitionProjectId: product.projectId,
            study: ctrl.studies[product.studyId],
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
      } else {
        // ensure that all validation errors are visible
        angular.forEach($scope.customerForm.$error, function(field) {
          angular.forEach(field, function(errorField) {
            if (errorField) {
              errorField.$setTouched();
            }
          });
        });
        SimpleMessageToastService.openAlertMessageToast(
          'shopping-cart.toasts.customer-has-validation-errors-toast');
      }
    };

    ctrl.clear = function() {
      ShoppingCartService.clear();
    };

    ctrl.init();
  });
