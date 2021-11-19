
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
                      // $translate,
                      AnalysisPackageSearchService,
                      // AnalysisPackageAccessWaysResource,
                      CurrentAnalysisPackageService
                      // DataPackageSearchService,
                      // DataPackageAccessWaysResource,
                      // DataPackageCitationDialogService,
                      // CurrentDataPackageService
  ) {
    var $ctrl = this;
    var initReady = false;
    $ctrl.analysisPackageIdVersion = {};
    // $ctrl.accessWays = [];
    $ctrl.lang = LanguageService.getCurrentInstantly();
    $ctrl.onAnalysisPackageChange = MessageBus;
    $ctrl.noFinalRelease = false;
    $ctrl.disabled = false;
    $scope.bowser = $rootScope.bowser;
    $ctrl.numberOfShoppingCartProducts = ShoppingCartService.count();

    function init() {
      var search = $location.search();
      if (search['access-way']) {
        $ctrl.selectedAccessWay = search['access-way'];
      }
      if (!$ctrl.selectedAccessWay) {
        $ctrl.selectedAccessWay = '';
      }
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
            // loadAccessWays(id);
          });
    }

    function loadAnalysisPackage(id, version) {
      $rootScope.$broadcast('start-ignoring-404');
      $ctrl.noFinalRelease = false;
      var excludes = ['nested*', 'variables', 'questions',
        'surveys', 'instruments', 'relatedPublications',
        'concepts'];
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
        }
      }, function() {
        $ctrl.analysisPackage = null;
        $rootScope.selectedAnalysisPackage = null;
      }).finally(function() {
        $rootScope.$broadcast('stop-ignoring-404');
      });
    }

    $ctrl.addToShoppingCart = function() {
        ShoppingCartService.add({
          dataAcquisitionProjectId: $ctrl.analysisPackage
          .dataAcquisitionProjectId,
          version: $ctrl.selectedVersion,
          analysisPackage: {
            id: $ctrl.analysisPackage.id,
            title: $ctrl.analysisPackage.title
          }
        });
    };
    var unregisterTransitionHook = $transitions.onStart({}, function(trans) {
      $ctrl.disabled = trans.$to().name === 'relatedPublicationDetail' ||
        trans.$to().name === 'conceptDetail';
    });

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
          $ctrl.analysisPackageIdVersion.masterId = data.masterId;
          $ctrl.analysisPackageIdVersion.version = data.version;
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
  }

  angular
    .module('metadatamanagementApp')
    .controller('AnalysisPackageConfiguratorController',
      Controller);

})();