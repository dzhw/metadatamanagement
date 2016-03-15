'use strict';
angular.module('metadatamanagementApp').service('CustomModal',
function($uibModal) {
  var modal = {
    openModal: function(text) {
      var returnValue;
      var modalInstance = $uibModal.open({
        templateUrl: 'scripts/components/modal/customModal.html.tmpl',
        animation: true,
        controller: function($uibModalInstance, $scope) {
          $scope.text = text;
          $scope.ok = function() {
            $uibModalInstance.close();
          };
          $scope.cancel = function() {
            $uibModalInstance.dismiss();
          };
        },
        size: 'lg'
      });
      modalInstance.result.then(function() {
        returnValue = true;
        return returnValue;
      }, function() {
        returnValue = false;
        console.log(returnValue);
        return returnValue;
      });
    }
  };
  return modal;
});
