'use strict';

angular.module('metadatamanagementApp').controller('ActivationController',
    function($scope, $stateParams, $location, $rootScope,
      BookmarkableUrl, Auth) {
      BookmarkableUrl.setUrlLanguage($location, $rootScope);
      Auth.activateAccount({
        key: $stateParams.key
      }).then(function() {
        $scope.error = null;
        $scope.success = 'OK';
      }).catch(function() {
        $scope.success = null;
        $scope.error = 'ERROR';
      });
    });
