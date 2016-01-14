'use strict';

xdescribe('Controllers Tests ', function () {
  var $scope, FdzProject, createController, $uibModalInstance, MockEntity, $stateParams;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _$stateParams_) {
      $scope = _$rootScope_.$new();
      MockEntity = jasmine.createSpy('MockEntity');
      $uibModalInstance = {
        dismiss: jasmine.createSpy('$uibModalInstance.cancel'),
        result: {
          then: jasmine.createSpy('$uibModalInstance.result.then')
        }
      };
      $stateParams = _$stateParams_;
      FdzProject = {
        get: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        },
        create: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        },
        update: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        }
      };
      var locals = {
        '$scope' : $scope,
        'FdzProject' : FdzProject,
        'entity': MockEntity ,
        '$uibModalInstance': $uibModalInstance,
        '$stateParams': $stateParams,
        'isCreateMode': true
      };
      createController = function() {
        return $controller('FdzProjectDialogController', locals);
      };
      spyOn(FdzProject, 'get').and.callThrough();
      spyOn(FdzProject, 'create').and.callThrough();
      spyOn(FdzProject, 'update').and.callThrough();
    });
   });
   describe('FdzProjectDialogController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call FdzProject.get',function(){
       $scope.load();
       expect(FdzProject.get).toHaveBeenCalled();
     });
     it('should call FdzProject.create',function(){
       $scope.save();
       expect(FdzProject.create).toHaveBeenCalled();
     });
     it('should call FdzProject.update',function(){
       $scope.isCreateMode = false;
       $scope.$apply();
       $scope.save();
       //expect(FdzProject.update).toHaveBeenCalled();
     });
     it('should call $uibModalInstance.dismiss',function(){
       $scope.clear();
       expect($uibModalInstance.dismiss).toHaveBeenCalled();
     });

   });
});
