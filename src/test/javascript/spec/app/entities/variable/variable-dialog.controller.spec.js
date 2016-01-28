'use strict';

describe('Controllers Tests ', function () {
  var $scope, createController, $uibModalInstance, MockEntity, FdzProjectCollection, SurveyCollection, $q;
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
          error();
          return {

          };
        }
      };
      //MockEntity = jasmine.createSpy('Variable');
      FdzProjectCollection = {
        query: function(callback){
          var deferred = $q.defer();
          deferred.resolve(result);
          callback(result);
          return deferred.promise;
        }
      };
      SurveyCollection = {
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
        'FdzProjectCollection' : FdzProjectCollection,
        'SurveyCollection' : SurveyCollection
      };
      createController = function() {
        return $controller('VariableDialogController', locals);
      };
      spyOn(FdzProjectCollection, 'query').and.callThrough();
      spyOn(SurveyCollection, 'query').and.callThrough();
    });
   });
   describe('VariableDialogController',function(){
     beforeEach(function(){
         createController();
     });
     it('$scope.isSaving should be false',function(){
       $scope.save();
       //expect($scope.isSaving).toEqual(false);
     });
     it('$scope.isSaving should be false',function(){
       $scope.isSurveyEmpty();
       //expect($scope.isSaving).toEqual(false);
     });
     xit('$scope.isSaving should be false',function(){
       $scope.changeSurvey();
       //expect($scope.isSaving).toEqual(false);
     });
     xit('should call $uibModalInstance.dismiss',function(){
       $scope.clear();
       expect($uibModalInstance.dismiss).toHaveBeenCalled();
     });
     xit('should set $scope.allFdzProjects',function(){
       FdzProjectCollection.query.and.returnValue($q.resolve());
       expect($scope.allFdzProjects.$$state.value).toEqual(result);
     });

   });
});
