'use strict';

angular.module('metadatamanagementApp')
  .controller('ReleaseProjectDialogController', function($scope, $mdDialog,
    project, SimpleMessageToastService, DataAcquisitionProjectResource,
    DataAcquisitionProjectReleaseResource) {
    $scope.project = project;
    var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
      'data-acquisition-project.';
    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    $scope.ok = function(release) {
      release.date = new Date().toISOString();
      project.release = release;
      DataAcquisitionProjectResource.save(project).$promise
        .then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
            i18nPrefix + 'released-successfully', {
              id: project.id
            });
        })
        .then(function() {
          DataAcquisitionProjectReleaseResource.release({
            id: project.id
          });
        });
      $mdDialog.hide();
    };
  });
