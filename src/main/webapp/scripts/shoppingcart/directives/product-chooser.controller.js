'use strict';

angular.module('metadatamanagementApp')
  .controller('ProductChooserController', [
    '$scope', 'Principal', 'DataAcquisitionProjectReleasesResource',
    'ShoppingCartService',
    function($scope, Principal,
      DataAcquisitionProjectReleasesResource, ShoppingCartService) {
      var ctrl = this;
      ctrl.accessWays = $scope.accessWays;
      ctrl.projectId = $scope.projectId;
      ctrl.studyId = $scope.studyId;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;

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
