'use strict';
angular.module('metadatamanagementApp').service('CustomModal',
function($uibModal, $q) {
  var getModal = function(text) {
    var deferred = $q.defer();
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
      deferred.resolve(returnValue);
    }, function() {
      returnValue = false;
      console.log(returnValue);
      deferred.resolve(returnValue);
    });
    return deferred.promise;
  };

  return {
    getModal: getModal
  };
});
