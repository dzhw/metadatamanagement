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
      ctrl.studyId = $scope.studyId;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      $scope.bowser = $rootScope.bowser;

      if (ctrl.accessWays.length > 0) {
        ctrl.selectedAccessWay = ctrl.accessWays[0];
      }

      DataAcquisitionProjectReleasesResource.get(
        {id: ctrl.projectId})
        .$promise.then(
          function(releases) {
            ctrl.releases = releases;
            if (releases.length > 0) {
              ctrl.selectedVersion = releases[0].version;
            }
          });

      ctrl.addToShoppingCart = function() {
        ShoppingCartService.add({
          projectId: ctrl.projectId,
          studyId: ctrl.studyId,
          accessWay: ctrl.selectedAccessWay,
          version: ctrl.selectedVersion,
        });
      };
    }
  ]);
