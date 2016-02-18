'use strict';

describe('Controllers Tests ', function () {
  var $scope, createController, $uibModalInstance, MockEntity;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      //MockEntity = jasmine.createSpy('MockEntity');
      MockEntity = {
        $save: function(success, error){
          success();
          error();
        }
      };
      $uibModalInstance = {
        dismiss: jasmine.createSpy('$uibModalInstance.cancel'),
        close: jasmine.createSpy('$uibModalInstance.close'),
        result: {
          then: jasmine.createSpy('$uibModalInstance.result.then')
        }
      };
      var locals = {
        '$scope' : $scope,
        'entity': MockEntity ,
        '$uibModalInstance': $uibModalInstance,
        'isCreateMode': true
      };
      createController = function() {
        return $controller('DataAcquisitionProjectDialogController', locals);
      };
    });
   });
   describe('DataAcquisitionProjectDialogController',function(){
     beforeEach(function(){
         createController();
     });
     it('$scope.isSaving should be false',function(){
       $scope.save();
       expect($scope.isSaving).toEqual(false);
     });
     it('should call $uibModalInstance.dismiss',function(){
       $scope.clear();
       expect($uibModalInstance.dismiss).toHaveBeenCalled();
     });

   });
});
