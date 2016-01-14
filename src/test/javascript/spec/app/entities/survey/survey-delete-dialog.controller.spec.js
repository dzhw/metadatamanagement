'use strict';

describe('Controllers Tests ', function () {
  var $scope, Survey, createController, $uibModalInstance, MockEntity;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      MockEntity = jasmine.createSpy('MockEntity');
      $uibModalInstance = {
        dismiss: jasmine.createSpy('$uibModalInstance.cancel'),
        result: {
          then: jasmine.createSpy('$uibModalInstance.result.then')
        }
      };
      Survey = {
        delete: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        }
      };
      var locals = {
        '$scope' : $scope,
        'Survey' : Survey,
        'entity': MockEntity ,
        '$uibModalInstance': $uibModalInstance
      };
      createController = function() {
        return $controller('SurveyDeleteController', locals);
      };
      spyOn(Survey, 'delete').and.callThrough();
    });
   });
   describe('SurveyDeleteController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call $uibModalInstance.dismiss',function(){
       $scope.clear();
       expect($uibModalInstance.dismiss).toHaveBeenCalled();
     });
     it('should call Survey.delete',function(){
       $scope.confirmDelete();
       expect(Survey.delete).toHaveBeenCalled();
     });

   });
});
