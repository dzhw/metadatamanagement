/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ProductChooserController', [
    '$scope', 'Principal', '$rootScope',
    'DataAcquisitionProjectReleasesResource', 'ShoppingCartService',
    function($scope, Principal, $rootScope,
      DataAcquisitionProjectReleasesResource, ShoppingCartService) {
      var ctrl = this;
      ctrl.accessWays = $scope.accessWays;
      ctrl.projectId = $scope.projectId;
      ctrl.study = $scope.study;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      $scope.bowser = $rootScope.bowser;
      ctrl.noDataSets = false;
      ctrl.noFinalRelease = false;
      ctrl.dataNotAvailable = false;

      if (ctrl.accessWays.length > 0) {
        if (_.includes(ctrl.accessWays, 'not-accessible')) {
          ctrl.variableNotAccessible = true;
        } else {
          ctrl.selectedAccessWay = ctrl.accessWays[0];
        }
      } else {
        ctrl.noDataSets = true;
      }

      if (ctrl.study.dataAvailability.en === 'Not available') {
        ctrl.dataNotAvailable = true;
      }

      if (ctrl.study.dataAvailability.en === 'In preparation') {
        ctrl.noFinalRelease = true;
      }

      DataAcquisitionProjectReleasesResource.get(
        {id: ctrl.projectId})
        .$promise.then(
          function(releases) {
            ctrl.releases = releases;
            if (releases.length > 0) {
              ctrl.selectedVersion = releases[0].version;
            } else {
              ctrl.noFinalRelease = true;
            }
          });

      ctrl.addToShoppingCart = function() {
        ShoppingCartService.add({
          projectId: ctrl.projectId,
          studyId: ctrl.study.id,
          accessWay: ctrl.selectedAccessWay,
          version: ctrl.selectedVersion,
        });
      };
    }
  ]);
