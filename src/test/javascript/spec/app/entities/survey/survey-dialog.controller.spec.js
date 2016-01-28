'use strict';

describe('Controllers Tests ', function () {
  var $scope, createController, $uibModalInstance, MockEntity, FdzProjectCollection, $q;
  var result = {
    'page' : {
      'totalElements':2
    },
    '_embedded' : {
      'fdzProjects':[]
    }
  };
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _$q_) {
      $scope = _$rootScope_.$new();
      $q = _$q_;
      MockEntity = {
        $save: function(success, error){
          success();
          error();
        }
      };
      FdzProjectCollection = {
        query: function(callback){
          var deferred = $q.defer();
          deferred.resolve(result);
          callback(result);
          return deferred.promise;
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
        'isCreateMode': true,
        'FdzProjectCollection' : FdzProjectCollection
      };
      createController = function() {
        return $controller('SurveyDialogController', locals);
      };
      spyOn(FdzProjectCollection, 'query').and.callThrough();
    });
   });
   describe('SurveyDialogController',function(){
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
     it('should set $scope.allFdzProjects',function(){
       FdzProjectCollection.query.and.returnValue($q.resolve());
       expect($scope.allFdzProjects.$$state.value).toEqual(result);
     });

   });
});
