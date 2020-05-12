/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
  function(PageTitleService, $state, BreadcrumbService,
           ShoppingCartService, $scope, StudyResource, DataSetSearchService,
           VariableSearchService, DataAcquisitionProjectReleasesResource, $q,
           OrderResource, LanguageService, SimpleMessageToastService, order,
           ProjectReleaseService, $rootScope) {

    PageTitleService.setPageTitle('shopping-cart.title');
    BreadcrumbService.updateToolbarHeader({
      'stateName': $state.current.name
    });
    var ctrl = this;
    var existingOrderId;
    ctrl.studies = {};
    ctrl.releases = {};
    ctrl.customer = {};
    ctrl.counts = {};
    ctrl.noShadowCopyAvailable = {};
    ctrl.initComplete = false;

    var appendVersionSuffix = function(product) {
      var suffixedProduct = _.cloneDeep(product);
      suffixedProduct.dataAcquisitionProjectId =
        product.dataAcquisitionProjectId + '-' + product.version;

      suffixedProduct.study.id = product.study.id + '-' + product.version;
      return suffixedProduct;
    };

    var initViewWithOrderResource = function(order) {
      order.$promise.then(function(order) {
        ShoppingCartService
          .initShoppingCartProducts(_.get(order, 'products', []), order.id,
            order.version);
      }, function(error) {
        if (error.status === 404) {
          ShoppingCartService.clearLocalOrderId();
        }
      }).finally(ctrl.init);
    };

    var loadDataSetCountForProduct = function(product, studyId) {
      return DataSetSearchService.countByMultiple({
        'studyId': studyId,
        'accessWays': product.accessWay
      }).then(function(result) {
        ctrl.counts[studyId + product.accessWay + product.version] =
          ctrl.counts[studyId + product.accessWay +
          product.version] || {};
        ctrl.counts[studyId + product.accessWay +
        product.version].dataSets = result.count;
      });
    };

    var loadVariablesCountForProduct = function(product, studyId) {
      return VariableSearchService.countByMultiple({
        'studyId': studyId,
        'accessWays': product.accessWay
      }).then(function(result) {
        ctrl.counts[studyId + product.accessWay + product.version] =
          ctrl.counts[studyId + product.accessWay +
          product.version] || {};
        ctrl.counts[studyId + product.accessWay +
        product.version].variables = result.count;
      });
    };

    var loadStudyAsMasterFallback = function(studyId) {
      var masterId = ProjectReleaseService.stripVersionSuffix(studyId);
      return StudyResource.get({id: masterId}).$promise.then(function(study) {
        ctrl.studies[studyId] = study;
        ctrl.noShadowCopyAvailable[studyId] = true;
      }, function(error) {
        return $q.reject(error);
      });
    };

    var loadStudy = function(studyId) {
      return StudyResource.get({id: studyId}).$promise.then(
        function(study) {
          ctrl.studies[studyId] = study;
        }, function(error) {
          if (error.status === 404) {
            return loadStudyAsMasterFallback(studyId);
          } else {
            return $q.reject(error);
          }
        });
    };

    var loadProjectReleases = function(projectId) {
      return DataAcquisitionProjectReleasesResource.get(
        {id: projectId}).$promise.then(function(releases) {
        if (releases.length > 0) {
          ctrl.releases[projectId] = releases[0];
        }
      });
    };

    ctrl.init = function() {
      var promises = [];
      existingOrderId = ShoppingCartService.getOrderId();
      ctrl.customer.name = _.get(order, 'customer.name');
      ctrl.customer.email = _.get(order, 'customer.email');
      ctrl.products = ShoppingCartService.getProducts();
      ctrl.products.forEach(function(product) {
        var studyId = product.study.id + '-' + product.version;
        ctrl.studies[studyId] = {};
        ctrl.releases[product.dataAcquisitionProjectId] = {};
        promises.push(loadDataSetCountForProduct(product, studyId));
        promises.push(loadVariablesCountForProduct(product, studyId));
      });

      $rootScope.$broadcast('start-ignoring-404');
      _.forEach(ctrl.studies, function(study, studyId) { // jshint ignore:line
        promises.push(loadStudy(studyId));
      });
      _.forEach(ctrl.releases,
        function(release, projectId) { // jshint ignore:line
          promises.push(loadProjectReleases(projectId));
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
      }).finally(function() {
        $rootScope.$broadcast('stop-ignoring-404');
      });
    };

    $scope.$on('shopping-cart-changed', function() {
      ctrl.products = ShoppingCartService.getProducts();
    });

    ctrl.getNumberOfVariables = function(product) {
      var suffixedProduct = appendVersionSuffix(product);
      return ctrl.counts[suffixedProduct.study.id + suffixedProduct.accessWay +
      suffixedProduct.version].variables;
    };

    ctrl.getNumberOfDataSets = function(product) {
      var suffixedProduct = appendVersionSuffix(product);
      return ctrl.counts[suffixedProduct.study.id + suffixedProduct.accessWay +
      suffixedProduct.version].dataSets;
    };

    ctrl.isCurrentVersion = function(product) {
      return ctrl.releases[product.dataAcquisitionProjectId].version ===
        product.version;
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
          state: 'CREATED',
          products: []
        };
        ctrl.products.forEach(function(product) {
          var completeProduct = {
            dataAcquisitionProjectId: product.dataAcquisitionProjectId,
            study: ctrl.studies[product.study.id + '-' + product.version],
            accessWay: product.accessWay,
            version: product.version
          };
          completeProduct.study.id = ProjectReleaseService
            .stripVersionSuffix(completeProduct.study.id);
          order.products.push(completeProduct);
        });

        var orderFn;
        var requestParams;

        if (existingOrderId) {
          order.version = ShoppingCartService.getVersion();
          orderFn = OrderResource.update;
          requestParams = {
            id: existingOrderId
          };
        } else {
          orderFn = OrderResource.save;
        }

        orderFn(requestParams, order).$promise.then(function() {
          ShoppingCartService.completeOrder();
          ctrl.orderSaved = true;
        }).catch(function() {
          SimpleMessageToastService.openAlertMessageToast(
            'shopping-cart.toasts.error-on-saving-order');
          // Perhaps our order data went stale, attempt reload in this case
          if (existingOrderId) {
            initViewWithOrderResource(OrderResource.get({id: existingOrderId}));
          }
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
      ShoppingCartService.clearProducts();
    };

    if (order) {
      initViewWithOrderResource(order);
    } else {
      ctrl.init();
    }
  });
