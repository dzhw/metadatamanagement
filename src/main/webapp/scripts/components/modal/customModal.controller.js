'use strict';

angular.module('metadatamanagementApp')
    .controller('CustomModalController', function($uibModalInstance, $scope) {
      $scope.ok = function() {
        console.log($uibModalInstance);
        $uibModalInstance.close();
      };
      $scope.cancel = function() {
        $uibModalInstance.dismiss();
      };
    });
