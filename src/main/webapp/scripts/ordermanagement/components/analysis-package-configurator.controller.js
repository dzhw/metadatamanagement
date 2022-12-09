/* global _ */
(function() {

  'use strict';

  function Controller($scope,
                      $rootScope,
                      $location,
                      DataAcquisitionProjectReleasesResource,
                      $state,
                      $mdDialog,
                      $transitions,
                      LanguageService,
                      ProjectReleaseService,
                      ShoppingCartService,
                      MessageBus,
                      AnalysisPackageSearchService,
                      CurrentAnalysisPackageService,
                      AnalysisPackageCitationDialogService,
                      DataPackageSearchService,
                      Principal
  ) {
    var $ctrl = this;
    var initReady = false;
    $ctrl.analysisPackageIdVersion = {};
    $ctrl.dataPackages = [];
    $ctrl.lang = LanguageService.getCurrentInstantly();
    $ctrl.onAnalysisPackageChange = MessageBus;
    $ctrl.noFinalRelease = false;
    $ctrl.disabled = false;
    $scope.bowser = $rootScope.bowser;
    $ctrl.numberOfShoppingCartProducts = ShoppingCartService.count();

    function init() {
      $ctrl.selectedVersion = $ctrl.analysisPackageIdVersion.version;
      loadAnalysisPackage($ctrl.analysisPackageIdVersion.masterId,
        $ctrl.analysisPackageIdVersion.version);
      initReady = true;
    }

    function loadVersion(dataAcquisitionProjectId) {
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

    function loadDataPackage(id, version, accessWay) {
      // $rootScope.$broadcast('start-ignoring-404');
      $ctrl.noFinalRelease = false;
      var excludes = ['nested*', 'variables', 'questions',
        'surveys', 'instruments', 'relatedPublications',
        'concepts'];
      DataPackageSearchService.findShadowByIdAndVersion(id, version, excludes)
        .promise.then(function(data) {
        data.selectedAccessWay = accessWay;
        data.selectedVersion = version;
        $ctrl.dataPackages.push(data);
      });
    }

    function loadAnalysisPackage(id, version) {
      $rootScope.$broadcast('start-ignoring-404');
      $ctrl.noFinalRelease = false;
      var excludes = ['nested*', 'variables', 'questions',
        'surveys', 'instruments', 'relatedPublications',
        'concepts'];
      if (version === undefined) {
        AnalysisPackageSearchService.findAnalysisPackageById(id, excludes)
        .promise.then(function(res) {
          $ctrl.analysisPackage = res;
          $rootScope.selectedDataPackage = res;
          if ($ctrl.analysisPackage) {
            loadVersion($ctrl.analysisPackageIdVersion.projectId, id);
          }
        }, function() {
          $ctrl.analysisPackage = null;
          $rootScope.selectedDataPackage = null;
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
      } else {
        AnalysisPackageSearchService
          .findShadowByIdAndVersion(id, version, excludes)
          .promise.then(function(data) {
          $ctrl.analysisPackage = data;
          $rootScope.selectedDataPackage = data;
          CurrentAnalysisPackageService.setCurrentAnalysisPackage(data);
          if ($ctrl.analysisPackage) {
            if ($rootScope.bowser.compareVersions(['1.0.0', version]) === 1) {
              $ctrl.noFinalRelease = true;
            }
            loadVersion($ctrl.analysisPackage.dataAcquisitionProjectId, id);
            $ctrl.dataPackages.length = 0;
            _.forEach($ctrl.analysisPackage.analysisDataPackages,
              function(item) {
              if (item.type === 'dataPackage') {
                loadDataPackage(
                  item.dataPackageMasterId,
                  item.version,
                  item.accessWay
                );
              }
            });
          }
        }, function() {
          $ctrl.analysisPackage = null;
          $rootScope.selectedAnalysisPackage = null;
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
      }
    }

    $ctrl.addToShoppingCart = function() {
      var productList = [];
      productList.push({
        dataAcquisitionProjectId: $ctrl.analysisPackage
          .dataAcquisitionProjectId,
        version: $ctrl.selectedVersion,
        analysisPackage: {
          id: $ctrl.analysisPackage.id,
          title: $ctrl.analysisPackage.title
        }
      });
      if ($ctrl.dataPackages.length) {
        _.uniqBy($ctrl.dataPackages, 'dataAcquisitionProjectId');
        _.forEach($ctrl.dataPackages, function(item) {
          productList.push({
            dataAcquisitionProjectId: item.dataAcquisitionProjectId,
            accessWay: item.selectedAccessWay,
            version: item.selectedVersion,
            dataFormats: extractDataFormats(item,
              item.selectedAccessWay),
            study: {
              id: item.id,
              surveyDataTypes: item.surveyDataTypes,
              title: item.title
            },
            dataPackage: {
              id: item.id,
              surveyDataTypes: item.surveyDataTypes,
              title: item.title
            }
          });
        });
      }
      $ctrl.dataPackages.length = 0;
      ShoppingCartService.add(productList);

    };
    var unregisterTransitionHook = $transitions.onStart({}, function(trans) {
      $ctrl.disabled = trans.$to().name === 'relatedPublicationDetail' ||
        trans.$to().name === 'conceptDetail';
    });

    $ctrl.showBackToEditButton = function() {
      return $ctrl.selectedVersion && Principal.hasAuthority(
        'ROLE_DATA_PROVIDER');
    };

    // closes the order menu in the parent component
    $ctrl.closeOrderMenu = function() {
      MessageBus.set('onCloseOrderMenu', {open: false});
    };

    $scope.$on('$destroy', function() {
      CurrentAnalysisPackageService.setCurrentAnalysisPackage(null);
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
        return $rootScope.currentLanguage;
      },
      function(newVal, oldVal) {
        if (newVal !== oldVal) {
          $ctrl.lang = $rootScope.currentLanguage;
        }
      });

    $scope.$watch(function() {
        return $ctrl.onAnalysisPackageChange;
      },
      function() {
        var data = $ctrl.onAnalysisPackageChange
          .get('onAnalysisPackageChange', true);
        if (data) {
          var versionFromUrl = $location.search().version;
          $ctrl.analysisPackageIdVersion.masterId = data.masterId;
          $ctrl.analysisPackageIdVersion.version = versionFromUrl;
          $ctrl.analysisPackageIdVersion.projectId = data.projectId;
          init();
        }
      }, true);

    $ctrl.showVersionHelp = function($event) {
      $mdDialog.show({
        controller: 'VersionInfoController',
        templateUrl: 'scripts/ordermanagement/views/' +
          'ap-version-info.html.tmpl',
        clickOutsideToClose: true,
        escapeToClose: true,
        fullscreen: true,
        targetEvent: $event
      });
    };

    $ctrl.openCitationDialog = function($event) {
      AnalysisPackageCitationDialogService.showDialog(
        $ctrl.analysisPackage, $event
      );
    };
  }

  angular
    .module('metadatamanagementApp')
    .controller('AnalysisPackageConfiguratorController',
      Controller);

})();
