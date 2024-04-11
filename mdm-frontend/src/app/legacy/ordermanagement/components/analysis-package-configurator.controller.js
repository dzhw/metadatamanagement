/* globals _ */
'use strict';

  angular
    .module('metadatamanagementApp')
    .controller('AnalysisPackageConfiguratorController', [
      '$scope',
      '$rootScope',
      '$location',
      'DataAcquisitionProjectReleasesResource',
      '$state',
      '$mdDialog',
      '$transitions',
      'LanguageService',
      'ProjectReleaseService',
      'ShoppingCartService',
      'MessageBus',
      'AnalysisPackageSearchService',
      'CurrentAnalysisPackageService',
      'AnalysisPackageCitationDialogService',
      'DataPackageSearchService',
      'Principal',
      'dataAcquisitionProjectSearchService',
      'ElasticSearchClient',
      function ($scope,
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
          Principal,
          dataAcquisitionProjectSearchService,
          ElasticSearchClient
  ) {
    var $ctrl = this;
    var initReady = false;
    $ctrl.analysisPackageIdVersion = {};
    $ctrl.dataPackages = [];
    $ctrl.lang = LanguageService.getCurrentInstantly();
    $ctrl.onAnalysisPackageChange = MessageBus;
    $ctrl.noFinalRelease = false;
    $ctrl.isPreReleased = false;
    $ctrl.disabled = false;
    $scope.bowser = $rootScope.bowser;
    $ctrl.numberOfShoppingCartProducts = ShoppingCartService.count();

    // if setting to true: do not forget to update translation texts
    $ctrl.showMaintenanceHint = false;

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
            var releaseList = [];
            for (var release of releases) {
                // only allow fully released versions to be listed for ordering
                if (!release.isPreRelease) {
                  releaseList.push(release);
                }
            }
            $ctrl.releases = releaseList;
            if (releases.length === 0 || rel.length === 0) {
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
            if ($ctrl.analysisPackage.release && $ctrl.analysisPackage.release.isPreRelease) {
              // disable ordering on case of pre-release
              $ctrl.isPreReleased = true;
            }
            // get project for embargo warning display
            var id = ProjectReleaseService.stripVersionSuffix(
              $ctrl.analysisPackage.dataAcquisitionProjectId
            );
            var projectQuery = dataAcquisitionProjectSearchService.createSearchQueryForProjectsById(
              "analysisPackages",
              false, //all projects
              id,
              null);
            ElasticSearchClient.search(projectQuery).then(function(results) {
              if (results.hits.hits.length === 1) {
                $ctrl.project = results.hits.hits[0]._source;
              } else {
                results.hits.hits.length < 1 ? 
                  console.error("No projects found") : 
                  console.error("Search resulted in more than one project being found.")
              } 
            });
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
            if ($ctrl.analysisPackage.release && $ctrl.analysisPackage.release.isPreRelease) {
              // disable ordering on case of pre-release
              $ctrl.isPreReleased = true;
            }
            // get project for embargo warning display
            var id = ProjectReleaseService.stripVersionSuffix(
              $ctrl.analysisPackage.dataAcquisitionProjectId
            );
            var projectQuery = dataAcquisitionProjectSearchService.createSearchQueryForProjectsById(
              "analysisPackages",
              false, //all projects
              id,
              null);
            ElasticSearchClient.search(projectQuery).then(function(results) {
              if (results.hits.hits.length === 1) {
                $ctrl.project = results.hits.hits[0]._source;
              } else {
                results.hits.hits.length < 1 ? 
                  console.error("No projects found") : 
                  console.error("Search resulted in more than one project being found.")
              } 
            });
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
          title: $ctrl.analysisPackage.title,
          doi: $ctrl.analysisPackage.doi
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
            dataPackage: {
              id: item.id,
              surveyDataTypes: item.surveyDataTypes,
              title: item.title,
              doi: item.doi
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

    // $ctrl.showBackToEditButton = function() {
    //   return $ctrl.selectedVersion && Principal.hasAuthority(
    //     'ROLE_DATA_PROVIDER');
    // };

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
        $ctrl.isPreReleased = false;
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

    /**
       * Whether the embargo date has expired or not.
       * @returns true if it has expired else false
       */
    $ctrl.isEmbargoDateExpired = function() {
      if ($ctrl.dataPackage.embargoDate) {
        var current = new Date();
        return new Date($ctrl.dataPackage.embargoDate) < current;
      }
      return true;
    };
}]);
