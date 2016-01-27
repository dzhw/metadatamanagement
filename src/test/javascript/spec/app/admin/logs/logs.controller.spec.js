'use strict';

describe('Controllers Tests ', function () {
  var $scope, LogsService, createController;
  var result = 'result';
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _LogsService_, $q) {
      $scope = _$rootScope_.$new();
      spyOn(_LogsService_, "changeLevel").and.callFake(function(par, callback) {
        callback();
        var deferred = $q.defer();
        deferred.resolve(result);
        return deferred.promise;
      });
      spyOn(_LogsService_, "findAll").and.callFake(function() {
        var deferred = $q.defer();
        deferred.resolve(result);
        return deferred.promise;
      });

      var locals = {
        '$scope' : $scope,
        'LogsService' : _LogsService_
      };
      createController = function() {
        return $controller('LogsController', locals);
      };
    });
   });
   describe('LogsController',function(){
     beforeEach(function(){
         createController();
     });
     it('should set $scope.loggers',function(){
         $scope.changeLevel('user',1);
         expect($scope.loggers.$$state.value).toEqual(result);
     });
   });
});
