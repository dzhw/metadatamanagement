/* global _ */
(function() {
    'use strict';

    function Controller(
      $rootScope,
      SearchDao,
      LanguageService,
      CurrentDataPackageService,
      DataPackageSearchService,
      DataAcquisitionProjectReleasesResource,
      ProjectReleaseService,
      DataPackageAccessWaysResource
    ) {
      var $ctrl = this;
      var initialize = false;
      $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      $ctrl.currentDataPackage = '';
      $ctrl.currentAccessWay = '';
      $ctrl.currentVersion = {};
      $ctrl.dataPackages = [];
      $ctrl.accessWays = [];
      $ctrl.releases = [];
      $ctrl.searchText = '';

      function loadDataPackage(id, version) {
        $rootScope.$broadcast('start-ignoring-404');
        var excludes = ['nested*', 'variables', 'questions',
          'surveys', 'instruments', 'relatedPublications',
          'concepts'];
        DataPackageSearchService.findShadowByIdAndVersion(id, version, excludes)
          .promise.then(function(data) {
          CurrentDataPackageService.setCurrentDataPackage(data);
          if (data) {
            if (!$ctrl.currentDataPackage || initialize === true) {
              $ctrl.currentDataPackage = data;
              $ctrl.currentVersion.version = $ctrl.package.version;
              $ctrl.currentAccessWay = $ctrl.package.accessWay;
            }
            loadVersion(data.dataAcquisitionProjectId, id);
          }
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
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
              if ($ctrl.releases.length === 1) {
                $ctrl.currentVersion = $ctrl.releases[0];
                $ctrl.package.version = $ctrl.currentVersion.version;
              }
              loadAccessWays(id);
            });
      }

      function loadAccessWays(id) {
        DataPackageAccessWaysResource.get({id: id})
          .$promise
          .then(function(data) {
            $ctrl.accessWays = data;
            if ($ctrl.accessWays.length === 1) {
              $ctrl.currentAccessWay = $ctrl.accessWays[0];
              $ctrl.package.accessWay = $ctrl.currentAccessWay;
            }
            initialize = false;
          });
      }

      $ctrl.search = function(query) {
        return SearchDao.search(
          query, 1, null, null,
          'data_packages', 50, null,
          null, null, null, true
        ).then(function(data) {
          var result = [];
          _.forEach(data.hits.hits, function(item) {
            result.push(item._source);
          });
          return result;
        });
      };

      $ctrl.selectedItem = function(item) {
        if (item) {
          $ctrl.package.dataPackageMasterId = item.masterId;
          if (!initialize) {
            $ctrl.currentVersion.version = '';
            $ctrl.currentAccessWay = '';
          }
          loadDataPackage(item.masterId, item.release.version);
        }
      };

      $ctrl.changeVersion = function() {
        $ctrl.package.version = $ctrl.currentVersion.version;
        $ctrl.currentAccessWay = '';
        loadAccessWays($ctrl.package.dataPackageMasterId);
      };

      $ctrl.changeAccessWay = function() {
        $ctrl.package.accessWay = $ctrl.currentAccessWay;
      };

      $ctrl.$onInit = function() {
        $ctrl.package = $ctrl.package || {};
        if ($ctrl.package.dataPackageMasterId && $ctrl.package.version) {
          initialize = true;
          loadDataPackage(
            $ctrl.package.dataPackageMasterId,
            $ctrl.package.version
          );
        }
      };
    }

    angular
      .module('metadatamanagementApp')
      .controller('dataPackageController', [
      '$rootScope',
      'SearchDao',
      'LanguageService',
      'CurrentDataPackageService',
      'DataPackageSearchService',
      'DataAcquisitionProjectReleasesResource',
      'ProjectReleaseService',
      'DataPackageAccessWaysResource',
      Controller
    ]);
  }

)
();
