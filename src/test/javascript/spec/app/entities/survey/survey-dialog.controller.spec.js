'use strict';

xdescribe('Controllers Tests ', function () {
  var $scope, Survey, FdzProject, createController, $uibModalInstance, MockEntity, $stateParams;

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
      Survey = {
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
      FdzProject = {
        query: function(){
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
        'Survey' : Survey,
        'entity': MockEntity ,
        '$uibModalInstance': $uibModalInstance,
        '$stateParams': $stateParams,
        'isCreateMode': true
      };
      createController = function() {
        return $controller('SurveyDialogController', locals);
      };
      spyOn(Survey, 'get').and.callThrough();
      spyOn(Survey, 'create').and.callThrough();
      spyOn(Survey, 'update').and.callThrough();
      spyOn(FdzProject, 'query').and.callThrough();
    });
   });
   describe('SurveyDialogController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call Survey.get',function(){
       $scope.load();
       expect(Survey.get).toHaveBeenCalled();
     });
     it('should call Survey.create',function(){
       $scope.save();
       expect(Survey.create).toHaveBeenCalled();
     });
     it('should call Survey.update',function(){
       $scope.isCreateMode = false;
       $scope.$apply();
       $scope.save();
      // expect(Survey.update).toHaveBeenCalled();
     });
     it('should call $uibModalInstance.dismiss',function(){
       $scope.clear();
       expect($uibModalInstance.dismiss).toHaveBeenCalled();
     });

   });
});
