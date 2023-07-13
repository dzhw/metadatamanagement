'use strict';

angular.module('metadatamanagementApp').controller('RequestResetController', [
  '$scope',
  '$state',
  'BreadcrumbService',
  '$timeout',
  'Auth',
  'PageMetadataService',
  function($scope, $state, BreadcrumbService, $timeout, Auth,
    PageMetadataService) {
    PageMetadataService.setPageTitle('user-management.reset.request.title');
    $scope.success = null;
    $scope.error = null;
    $scope.errorEmailNotExists = null;
    $scope.resetAccount = {};
    $timeout(function() {
      angular.element('[ng-model="resetAccount.email"]').focus();
    });

    $scope.requestReset = function() {

      $scope.error = null;
      $scope.errorEmailNotExists = null;

      Auth.resetPasswordInit($scope.resetAccount.email).then(function() {
        $scope.success = 'OK';
      }).catch(function(response) {
        $scope.success = null;
        if (response.status === 400 &&
          response.data === 'e-mail address not registered') {
          $scope.errorEmailNotExists = 'ERROR';
        } else {
          $scope.error = 'ERROR';
        }
      });
    };
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  }]);

