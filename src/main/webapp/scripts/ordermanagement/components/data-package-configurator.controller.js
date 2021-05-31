/* global _ */
(function() {

  'use strict';

  function DataPackageConfiguratorController($scope,
                                $rootScope,
                                $location,
                                DataAcquisitionProjectReleasesResource,
                                $state,
                                $transitions,
                                LanguageService,
                                ProjectReleaseService,
                                ShoppingCartService,
                                MessageBus, $translate,
                                DataPackageSearchService,
                                DataPackageAccessWaysResource, $mdDialog,
                                DataPackageCitationDialogService,
                                CurrentDataPackageService) {
    var $ctrl = this;
    var initReady = false;
    $ctrl.dataPackageIdVersion = {};
    $ctrl.accessWays = [];
    $ctrl.lang = LanguageService.getCurrentInstantly();
    $ctrl.onDataPackageChange = MessageBus;
    $ctrl.noFinalRelease = false;
    $ctrl.variableNotAccessible = false;
    $ctrl.disabled = false;
    $scope.bowser = $rootScope.bowser;
    $ctrl.numberOfShoppingCartProducts = ShoppingCartService.count();
    $ctrl.exportFormats = [{
      format: 'oai_dc',
      label: 'Dublin Core'
    }, {
      format: 'dara',
      label: 'da|ra'
    }, {
      format: 'oai_ddi31',
      label: 'DDI 3.1'
    }, {
      format: 'oai_ddi32',
      label: 'DDI 3.2'
    }, {
      format: 'data_cite_xml',
      label: 'DataCite (XML)'
    }, {
      format: 'data_cite_json',
      label: 'DataCite (JSON)'
    }, {
      format: 'schema_org_json_ld',
      label: 'Schema.org'
    }];

    function init() {
      var search = $location.search();
      if (search['access-way']) {
        $ctrl.selectedAccessWay = search['access-way'];
      }
      if (!$ctrl.selectedAccessWay) {
        $ctrl.selectedAccessWay = '';
      }
      $ctrl.selectedVersion = $ctrl.dataPackageIdVersion.version;
      loadDataPackage($ctrl.dataPackageIdVersion.masterId,
         $ctrl.dataPackageIdVersion.version);
      initReady = true;
    }

    function loadVersion(dataAcquisitionProjectId, id) {
      DataAcquisitionProjectReleasesResource.get(
        {
          id: ProjectReleaseService.stripVersionSuffix(
            dataAcquisitionProjectId
          )
        })
        .$promise
        .then(
        function(releases) {
          $ctrl.releases = releases;
          if (releases.length === 0) {
            $ctrl.noFinalRelease = true;
          }
          loadAccessWays(id);
        });
    }

    function loadDataPackage(id, version) {
      $rootScope.$broadcast('start-ignoring-404');
      $ctrl.noFinalRelease = false;
      var excludes = ['nested*','variables','questions',
        'surveys','instruments', 'relatedPublications',
        'concepts'];
      DataPackageSearchService.findShadowByIdAndVersion(id, version, excludes)
        .promise.then(function(data) {
          $ctrl.dataPackage = data;
          $rootScope.selectedDataPackage = data;
          CurrentDataPackageService.setCurrentDataPackage(data);
          if ($ctrl.dataPackage) {
            if ($rootScope.bowser.compareVersions(['1.0.0', version]) === 1) {
              $ctrl.noFinalRelease = true;
            }
            loadVersion($ctrl.dataPackage.dataAcquisitionProjectId, id);
          }
        }, function() {
          $ctrl.dataPackage = null;
          $rootScope.selectedDataPackage = null;
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
    }

    function loadAccessWays(id) {
      DataPackageAccessWaysResource.get({id: id})
        .$promise
        .then(function(data) {
          $ctrl.accessWays = data;
          if ($ctrl.accessWays.length === 1) {
            $ctrl.selectedAccessWay = $ctrl.accessWays[0];
          }
        });
    }

    var extractDataFormats = function(dataPackage, selectedAccessWay) {
      var dataFormats = _.flatMap(dataPackage.dataSets, function(dataSet) {
        var subDataSetsBySelectedAccessWay = _.filter(dataSet.subDataSets,
          function(subDataSet) {
            return subDataSet.accessWay === selectedAccessWay;
          });
        return _.flatMap(subDataSetsBySelectedAccessWay,
          function(subDataSet) {
            return subDataSet.dataFormats;
          });
      });
      return _.uniq(dataFormats);
    };

    $ctrl.addToShoppingCart = function($event) {
      if (!$ctrl.selectedAccessWay) {
        var alert = $mdDialog.alert({
          title: $translate.instant(
            'shopping-cart.detail.select-access-way-title'),
          textContent: $translate.instant(
            'shopping-cart.detail.select-access-way-for-ordering'),
          ok: $translate.instant('global.buttons.close'),
          targetEvent: $event,
          clickOutsideToClose: true,
          escapeToClose: true,
          fullscreen: true
        });
        $mdDialog.show(alert);
      } else {
        ShoppingCartService.add({
          dataAcquisitionProjectId: $ctrl.dataPackage.dataAcquisitionProjectId,
          accessWay: $ctrl.selectedAccessWay,
          version: $ctrl.selectedVersion,
          dataFormats: extractDataFormats($ctrl.dataPackage,
            $ctrl.selectedAccessWay),
          study: {
            id: $ctrl.dataPackage.id,
            surveyDataTypes: $ctrl.dataPackage.surveyDataTypes,
            title: $ctrl.dataPackage.title
          }
        });
      }
    };
    var unregisterTransitionHook = $transitions.onStart({}, function(trans) {
      $ctrl.disabled = trans.$to().name === 'relatedPublicationDetail' ||
        trans.$to().name === 'conceptDetail';
    });

    $scope.$on('$destroy', function() {
      CurrentDataPackageService.setCurrentDataPackage(null);
      unregisterTransitionHook();
    });

    $scope.$on('shopping-cart-changed', function() {
      $ctrl.numberOfShoppingCartProducts = ShoppingCartService.count();
    });

    $scope.$watch(function() {
      return $ctrl.selectedVersion;
    }, function(newVal) {
      var search = $location.search();
      if (!newVal) { return; }
      if (newVal !== search.version) {
        search.version = $ctrl.selectedVersion;
        search.lang = $rootScope.currentLanguage;
        $state.go($state.current, search, {reload: true});
      }
    });

    $scope.$watch(function() {
      return $ctrl.selectedAccessWay;
    }, function(newVal) {
      var search = $location.search();
      if (initReady && newVal !== search['access-way']) {
        search['access-way'] = $ctrl.selectedAccessWay;
        if (search['access-way'] === '') {
          delete search['access-way'];
        }
        $location.search(search).replace();
      }
    });

    $scope.$watch(function() {
        return $rootScope.currentLanguage;
      },
      function(newVal, oldVal) {
        if (newVal !== oldVal) {
          $ctrl.lang = $rootScope.currentLanguage;
        }
      });

    $scope.$watch(function() {
        return $ctrl.onDataPackageChange;
      },
      function() {
        var data = $ctrl.onDataPackageChange.get('onDataPackageChange', true);
        if (data) {
          $ctrl.dataPackageIdVersion.masterId = data.masterId;
          $ctrl.dataPackageIdVersion.version = data.version;
          init();
        }
      }, true);

    $ctrl.showVersionHelp = function($event) {
      $mdDialog.show({
        controller: 'VersionInfoController',
        templateUrl: 'scripts/ordermanagement/views/' +
            'version-info.html.tmpl',
        clickOutsideToClose: true,
        escapeToClose: true,
        fullscreen: true,
        targetEvent: $event
      });
    };

    $ctrl.showAccessWayHelp = function($event) {
      $mdDialog.show({
        controller: 'AccessWayInfoController',
        templateUrl: 'scripts/ordermanagement/views/' +
            'access-way-info.html.tmpl',
        clickOutsideToClose: true,
        fullscreen: true,
        escapeToClose: true,
        targetEvent: $event
      });
    };

    $ctrl.openCitationDialog = function($event) {
      if (!$ctrl.selectedAccessWay) {
        var alert = $mdDialog.alert({
          title: $translate.instant(
            'shopping-cart.detail.select-access-way-title'),
          textContent: $translate.instant(
            'shopping-cart.detail.select-access-way-for-citation'),
          ok: $translate.instant('global.buttons.close'),
          targetEvent: $event,
          clickOutsideToClose: true,
          escapeToClose: true,
          fullscreen: true
        });
        $mdDialog.show(alert);
      } else {
        DataPackageCitationDialogService.showDialog($ctrl.selectedAccessWay,
            $ctrl.dataPackage, $event);
      }
    };
  }

  angular
    .module('metadatamanagementApp')
    .controller('DataPackageConfiguratorController',
      DataPackageConfiguratorController);

})();
