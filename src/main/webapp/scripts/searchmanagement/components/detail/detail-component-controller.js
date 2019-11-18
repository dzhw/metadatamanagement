/* global _ */

'use strict';

var CTRL = function(DataAcquisitionProjectReleasesResource,
                    ProjectReleaseService, ShoppingCartService) {
  var $ctrl = this;
  $ctrl.$onInit = init;
  $ctrl.noDataSets = false;
  $ctrl.noFinalRelease = false;
  $ctrl.dataNotAvailable = false;

  function init() {
    if ($ctrl.access.length > 0) {
      if (_.includes($ctrl.access, 'not-accessible')) {
        $ctrl.variableNotAccessible = true;
      } else {
        $ctrl.selectedAccessWay = $ctrl.access[0];
      }
    } else {
      $ctrl.noDataSets = true;
    }

    if ($ctrl.options.dataAvailability.en === 'Not available') {
      $ctrl.dataNotAvailable = true;
    }

    if ($ctrl.options.dataAvailability.en === 'In preparation') {
      $ctrl.noFinalRelease = true;
    }
    console.log($ctrl.dataAcquisitionProjectId);
    DataAcquisitionProjectReleasesResource.get(
      {
        id: ProjectReleaseService.stripVersionSuffix(
          $ctrl.options.dataAcquisitionProjectId
        )
      })
      .$promise.then(
      function(releases) {
        $ctrl.releases = releases;
        if (releases.length > 0) {
          if ($ctrl.options.version) {
            $ctrl.selectedVersion = $ctrl.options.version;
          } else {
            $ctrl.selectedVersion = releases[0].version;
          }
        } else {
          $ctrl.noFinalRelease = true;
        }
      });
  }
  $ctrl.addToShoppingCart = function() {
    ShoppingCartService.add({
      dataAcquisitionProjectId: $ctrl.options.dataAcquisitionProjectId,
      accessWay: $ctrl.selectedAccessWay,
      version: $ctrl.selectedVersion,
      study: {
        id: $ctrl.options.id
      }
    });
  };
};

angular
  .module('metadatamanagementApp')
  .controller('DataPacketController', CTRL);
