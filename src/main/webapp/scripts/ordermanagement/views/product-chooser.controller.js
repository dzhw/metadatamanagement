/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ProductChooserController', [
    '$scope', 'Principal', '$rootScope', '$mdDialog',
    'DataAcquisitionProjectReleasesResource', 'ShoppingCartService',
    'ProjectReleaseService', 'projectId', 'accessWays', 'study', 'version',
    function($scope, Principal, $rootScope, $mdDialog,
      DataAcquisitionProjectReleasesResource, ShoppingCartService,
      ProjectReleaseService, projectId, accessWays, study, version) {
      var ctrl = this;
      ctrl.accessWays = accessWays;
      ctrl.projectId = projectId;
      ctrl.study = study;
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
      var extractDataFormats = function(study) {
        var dataFormats = _.flatMap(study.dataSets, function(dataSet) {
          return _.flatMap(dataSet.subDataSets, function(subDataSet) {
            return subDataSet.dataFormats;
          });
        });
        return _.uniq(dataFormats);
      };

      DataAcquisitionProjectReleasesResource.get(
        {id: ProjectReleaseService.stripVersionSuffix(ctrl.projectId)})
        .$promise.then(
          function(releases) {
            ctrl.releases = releases;
            if (releases.length > 0) {
              if (version) {
                ctrl.selectedVersion = version;
              } else {
                ctrl.selectedVersion = releases[0].version;
              }
            } else {
              ctrl.noFinalRelease = true;
            }
          });

      ctrl.addToShoppingCart = function() {
        ShoppingCartService.add({
          dataAcquisitionProjectId: ctrl.projectId,
          accessWay: ctrl.selectedAccessWay,
          version: ctrl.selectedVersion,
          dataFormats: extractDataFormats(ctrl.study),
          study: {
            id: ctrl.study.id,
            surveyDataType: ctrl.study.surveyDataType
          }
        });
        $mdDialog.hide();
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.ok = function() {
        $mdDialog.hide();
      };
    }
  ]);
