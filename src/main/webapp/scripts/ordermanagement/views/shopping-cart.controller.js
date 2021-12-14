/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
  function(PageMetadataService, $state, BreadcrumbService,
           ShoppingCartService, $scope, DataPackageResource,
           DataSetSearchService, AnalysisPackageResource,
           VariableSearchService, DataAcquisitionProjectReleasesResource, $q,
           OrderResource, LanguageService, SimpleMessageToastService, order,
           $window, $interval, $location, $transitions, ProjectReleaseService,
           $rootScope, $document) {

    PageMetadataService.setPageTitle('shopping-cart.title');
    BreadcrumbService.updateToolbarHeader({
      'stateName': $state.current.name
    });
    var ctrl = this;
    var existingOrderId;
    var intervalReference;
    ctrl.dataAcquisitionProjects = {};
    ctrl.dataPackages = {};
    ctrl.analysisPackages = {};
    ctrl.softwarePackages = [];
    ctrl.releases = {};
    ctrl.counts = {};
    ctrl.noShadowCopyAvailable = {};
    ctrl.initComplete = false;
    ctrl.redirectCountDownSeconds = 5;

    $scope.$on('$destroy', function() {
      if (intervalReference) {
        $interval.cancel(intervalReference);
      }
    });

    var redirectAfterCountDown = function(interval, location, languageKey,
                                          orderId) {
      interval.then(function() {
        $interval.cancel(interval);
        $transitions.onBefore({entering: 'restoreShoppingCart'}, function() {
          return $q.defer().promise;
        });
        $location.path('/' + languageKey + '/shopping-cart/' + orderId)
          .replace();
        if (location) {
          $window.open(location, '_self');
        }
      });
    };

    var appendVersionSuffix = function(product) {
      var suffixedProduct = _.cloneDeep(product);
      suffixedProduct.dataAcquisitionProjectId =
        product.dataAcquisitionProjectId + '-' + product.version;

      suffixedProduct.study.id = product.study.id + '-' +
        product.version;
      suffixedProduct.dataPackage.id = product.dataPackage.id + '-' +
        product.version;
      return suffixedProduct;
    };

    var initViewWithOrderResource = function(order) {
      $rootScope.$broadcast('start-ignoring-404');
      var isCompletedOrder = false;
      order.$promise.then(function(order) {
        if (order.state === 'ORDERED') {
          ShoppingCartService.completeOrder();
          isCompletedOrder = true;
        } else {
          ShoppingCartService
            .initShoppingCartProducts(_.get(order, 'products', []), order.id,
              order.version);
        }
      }, function(error) {
        if (error.status === 404) {
          ShoppingCartService.clearLocalOrderId();
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'shopping-cart.error.synchronize');
        }
      }).finally(function() {
        $rootScope.$broadcast('stop-ignoring-404');
        if (isCompletedOrder) {
          $state.go('shoppingCart');
        } else {
          ctrl.init();
        }
      });
    };

    var loadDataSetCountForProduct = function(product, dataPackageId) {
      return DataSetSearchService.countByMultiple({
        'dataPackageId': dataPackageId,
        'accessWays': product.accessWay
      }).then(function(result) {
        ctrl.counts[dataPackageId + product.accessWay + product.version] =
          ctrl.counts[dataPackageId + product.accessWay +
          product.version] || {};
        ctrl.counts[dataPackageId + product.accessWay +
        product.version].dataSets = result.count;
      });
    };

    var loadVariablesCountForProduct = function(product, dataPackageId) {
      return VariableSearchService.countByMultiple({
        'dataPackageId': dataPackageId,
        'accessWays': product.accessWay
      }).then(function(result) {
        ctrl.counts[dataPackageId + product.accessWay + product.version] =
          ctrl.counts[dataPackageId + product.accessWay +
          product.version] || {};
        ctrl.counts[dataPackageId + product.accessWay +
        product.version].variables = result.count;
      });
    };

    var loadDataPackageAsMasterFallback = function(dataPackageId) {
      var masterId = ProjectReleaseService.stripVersionSuffix(dataPackageId);
      return DataPackageResource.get({id: masterId}).$promise.then(
        function(dataPackage) {
          ctrl.dataPackages[dataPackageId] = dataPackage;
          ctrl.noShadowCopyAvailable[dataPackageId] = true;
        }, function(error) {
        return $q.reject(error);
      });
    };

    var loadDataPackage = function(dataPackageId) {
      return DataPackageResource.get({id: dataPackageId}).$promise.then(
        function(dataPackage) {
          ctrl.dataPackages[dataPackageId] = dataPackage;
        }, function(error) {
          if (error.status === 404) {
            return loadDataPackageAsMasterFallback(dataPackageId);
          } else {
            return $q.reject(error);
          }
        });
    };

    var loadAnalysisPackageAsMasterFallback = function(analysisPackageId) {
      var masterId = ProjectReleaseService
        .stripVersionSuffix(analysisPackageId);
      return AnalysisPackageResource.get({id: masterId}).$promise.then(
        function(analysisPackage) {
          ctrl.analysisPackages[analysisPackageId] = analysisPackage;
          ctrl.noShadowCopyAvailable[analysisPackageId] = true;
        }, function(error) {
          return $q.reject(error);
        });
    };

    var loadAnalysisPackage = function(analysisPackageId) {
      return AnalysisPackageResource.get({id: analysisPackageId}).$promise.then(
        function(analysisPackage) {
          ctrl.analysisPackages[analysisPackageId] = analysisPackage;
        }, function(error) {
          if (error.status === 404) {
            return loadAnalysisPackageAsMasterFallback(analysisPackageId);
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

    var getDataPackagesInProducts = function(product, promises) {
      var dataPackageId = product.dataPackage.id + '-' + product.version;
      ctrl.dataPackages[dataPackageId] = {};
      ctrl.releases[product.dataAcquisitionProjectId] = {};
      promises.push(loadDataSetCountForProduct(product, dataPackageId));
      promises.push(loadVariablesCountForProduct(product, dataPackageId));
    };

    var getAnalysisPackagesInProducts = function(product) {
      var analysisPackageId = product.
        analysisPackage.id + '-' + product.version;
      ctrl.analysisPackages[analysisPackageId] = {};
      ctrl.releases[product.dataAcquisitionProjectId] = {};
    };

    ctrl.init = function() {
      // ShoppingCartService.clearProducts();
      var promises = [];
      existingOrderId = ShoppingCartService.getOrderId();
      ctrl.products = ShoppingCartService.getProducts();
      ctrl.products.forEach(function(product) {
        if (product.hasOwnProperty('dataPackage')) {
          getDataPackagesInProducts(product, promises);
        }
        if (product.hasOwnProperty('analysisPackage')) {
          getAnalysisPackagesInProducts(product, promises);
        }
      });

      $rootScope.$broadcast('start-ignoring-404');
      _.forEach(ctrl.analysisPackages,
        function(analysisPackage, analysisPackageId) { // jshint ignore:line
          promises.push(loadAnalysisPackage(analysisPackageId, promises));
        });
      _.forEach(ctrl.dataPackages,
          function(dataPackage, dataPackageId) { // jshint ignore:line
        promises.push(loadDataPackage(dataPackageId));
      });
      _.forEach(ctrl.releases,
        function(release, projectId) { // jshint ignore:line
          promises.push(loadProjectReleases(projectId));
        });
      $q.all(promises).then(function() {
        ctrl.initComplete = true;
        // remove all product which are not available anymore
        ctrl.products.forEach(function(product) {
          if (product.hasOwnProperty('dataPackage')) {
            if (ctrl.dataPackages[product.dataPackage.id].hidden) {
              ShoppingCartService.remove(product);
            }
          }
          if (product.hasOwnProperty('analysisPackage')) {
            if (ctrl.analysisPackages[product.analysisPackage.id].hidden) {
              ShoppingCartService.remove(product);
            }
          }
        });
      }).finally(function() {
        $rootScope.$broadcast('stop-ignoring-404');
      });
    };

    ctrl.getAccessibleCustomDataPackages = function(id) {
      var avail = {de: 'verfügbar', en: 'accessible'};
      var lang = LanguageService.getCurrentInstantly();
      return _.sumBy(ctrl.analysisPackages[id].analysisDataPackages,
        function(o) {
          return o.type === 'customDataPackage' &&
            o.availabilityType[lang] === avail[lang];
        });
    };

    ctrl.containsQuantitativeData = function(surveyDataTypes) {
      var containsQuantitativeData = false;
      surveyDataTypes.forEach(function(dataType) {
        if (dataType.en === 'Quantitative Data') {
          containsQuantitativeData = true;
        }
      });
      return containsQuantitativeData;
    };

    $scope.$on('shopping-cart-changed', function() {
      ctrl.products = ShoppingCartService.getProducts();
    });

    ctrl.getSoftwarePackages = function(product) {
      var result = [];
      _.forEach(ctrl.analysisPackages[product.
        analysisPackage.id + '-' + product.version].scripts,
        function(item) {
        result.push(item.softwarePackage);
      });
      return result;
    };

    ctrl.getNumberOfScripts = function(product) {
      return ctrl.analysisPackages[product.
        analysisPackage.id + '-' + product.version].scripts.length;
    };

    ctrl.getNumberOfVariables = function(product) {
      var suffixedProduct = appendVersionSuffix(product);
      return ctrl.counts[suffixedProduct.dataPackage.id +
        suffixedProduct.accessWay + suffixedProduct.version].variables;
    };

    ctrl.getNumberOfDataSets = function(product) {
      var suffixedProduct = appendVersionSuffix(product);
      return ctrl.counts[suffixedProduct.dataPackage.id +
        suffixedProduct.accessWay + suffixedProduct.version].dataSets;
    };

    ctrl.isCurrentVersion = function(product) {
      return ctrl.releases[product.dataAcquisitionProjectId].version ===
        product.version;
    };

    ctrl.deleteProduct = function(product) {
      ShoppingCartService.remove(product);
    };
    var createDataPackageForOrder = function(product, order) {
      var completeProduct = {
        dataAcquisitionProjectId: product.dataAcquisitionProjectId,
        study: ctrl.dataPackages[product.study.id + '-' +
        product.version],
        dataPackage: ctrl.dataPackages[product.dataPackage.id + '-' +
        product.version],
        accessWay: product.accessWay,
        version: product.version,
        dataFormats: product.dataFormats
      };
      _.set(completeProduct, 'study.surveyDataTypes',
        product.study.surveyDataTypes);
      _.set(completeProduct, 'dataPackage.surveyDataTypes',
        product.dataPackage.surveyDataTypes);
      completeProduct.study.id = ProjectReleaseService
        .stripVersionSuffix(completeProduct.study.id);
      completeProduct.dataPackage.id = ProjectReleaseService
        .stripVersionSuffix(completeProduct.dataPackage.id);
      order.products.push(completeProduct);
    };
    var createAnalysisPackageForOrder = function(product, order) {
      var completeProduct = {
        dataAcquisitionProjectId: product.dataAcquisitionProjectId,
        analysisPackage: ctrl
          .analysisPackages[product.analysisPackage.id + '-' + product.version],
        version: product.version,
      };
      completeProduct.analysisPackage.id = ProjectReleaseService
        .stripVersionSuffix(completeProduct.analysisPackage.id);
      order.products.push(completeProduct);
      console.log(order);
    };
    ctrl.order = function() {
      // check honeypot fields
      var email = $document.find('#email')[0].value;
      var website = $document.find('#website')[0].value;
      if (!website && email === 'your@email.com') {
        var order = {
          languageKey: LanguageService.getCurrentInstantly(),
          client: 'MDM',
          state: 'CREATED',
          products: []
        };
        ctrl.products.forEach(function(product) {
          if (product.hasOwnProperty('dataPackage')) {
            createDataPackageForOrder(product, order);
          }
          if (product.hasOwnProperty('analysisPackage')) {
            createAnalysisPackageForOrder(product, order);
          }
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
        order.client = 'MDM';
        orderFn(requestParams, order,
          function(responseData, headerGetter) {
            ShoppingCartService.completeOrder();
            ctrl.orderSaved = true;
            ctrl.redirectCountDownSeconds = 5;

            intervalReference = $interval(function() {
              ctrl.redirectCountDownSeconds = ctrl.redirectCountDownSeconds - 1;
            }, 1000, ctrl.redirectCountDownSeconds);

            redirectAfterCountDown(intervalReference, headerGetter('Location'),
              order.languageKey, responseData.id);
          }, function() {
            SimpleMessageToastService.openAlertMessageToast(
              'shopping-cart.toasts.error-on-saving-order');
            // Perhaps our order data went stale, attempt reload in this case
            if (existingOrderId) {
              initViewWithOrderResource(OrderResource
                .get({id: existingOrderId}));
            }
          });
      } else {
        // ensure that all validation errors are visible
        angular.forEach($scope.orderForm.$error, function(field) {
          angular.forEach(field, function(errorField) {
            if (errorField) {
              errorField.$setTouched();
            }
          });
        });
        SimpleMessageToastService.openAlertMessageToast(
          'shopping-cart.toasts.order-has-validation-errors-toast');
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
