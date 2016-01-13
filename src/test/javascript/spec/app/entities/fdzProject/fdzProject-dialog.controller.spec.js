'use strict';
// $uibModalInstance error
xdescribe('Controllers Tests ', function () {
  var $scope, FdzProject, createController, $uibModalInstance, $stateParams;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _$uibModalInstance_, _$stateParams_) {
      $scope = _$rootScope_.$new();
      $uibModalInstance = _$uibModalInstance_;
      $stateParams = _$stateParams_;
      FdzProject = {
        get: function(){
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
        '$uibModalInstance': $uibModalInstance,
        '$stateParams': $stateParams
      };
      createController = function() {
        return $controller('FdzProjectDialogController', locals);
      };
      spyOn(FdzProject, 'get').and.callThrough();
    });
   });
   describe('FdzProjectDialogController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call FdzProject.get',function(){
     });

   });
});
