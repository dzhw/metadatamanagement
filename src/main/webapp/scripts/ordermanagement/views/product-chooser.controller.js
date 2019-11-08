/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ProductChooserController', [
    '$scope', 'Principal', '$rootScope', '$mdDialog',
    'DataAcquisitionProjectReleasesResource', 'ShoppingCartService',
    'ProjectReleaseService', 'projectId', 'accessWays', 'study', 'version',
    'StudySearchService', 'StudyIdBuilderService', '$log',
    function($scope, Principal, $rootScope, $mdDialog,
      DataAcquisitionProjectReleasesResource, ShoppingCartService,
      ProjectReleaseService, projectId, accessWays, study, version,
      StudySearchService, StudyIdBuilderService, $log) {
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
      ctrl.isStudyQueryResolved = false;

      if (angular.isUndefined(ctrl.study.surveyDataTypes) ||
        angular.isUndefined(ctrl.study.dataSets)) {
        StudySearchService.findOneById(ctrl.study.id).promise
          .then(function(study) {
          ctrl.study = study;
          ctrl.isStudyQueryResolved = true;
        });
      } else {
        ctrl.isStudyQueryResolved = true;
      }

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
      var extractDataFormats = function(study, selectedAccessWay) {
        var dataFormats = _.flatMap(study.dataSets, function(dataSet) {
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

      var _addToShoppingCart = function() {
        ShoppingCartService.add({
          dataAcquisitionProjectId: ctrl.projectId,
          accessWay: ctrl.selectedAccessWay,
          version: ctrl.selectedVersion,
          dataFormats: extractDataFormats(ctrl.study, ctrl.selectedAccessWay),
          study: {
            id: ctrl.study.id,
            surveyDataTypes: ctrl.study.surveyDataTypes,
            title: ctrl.study.title
          }
        });
        $mdDialog.hide();
      };

      ctrl.addToShoppingCart = function() {
        // get the right shadow copy
        if (ctrl.study.release.version !== ctrl.selectedVersion) {
          var studyId = StudyIdBuilderService.buildStudyId(
            ProjectReleaseService.stripVersionSuffix(ctrl.projectId),
            ctrl.selectedVersion);
          StudySearchService.findOneById(studyId).promise.then(function() {
                _addToShoppingCart();
              }).catch(function(error) {
                if (error.status === 404) {
                  // there might be no shadow copy
                  _addToShoppingCart();
                } else {
                  $log.error('Error while retrieving shadow for "' + studyId +
                  '".', error);
                }
              });
        } else {
          _addToShoppingCart();
        }
      };

      $scope.closeDialog = function() {
        $mdDialog.cancel();
      };

      $scope.ok = function() {
        $mdDialog.hide();
      };
    }
  ]);
