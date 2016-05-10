/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */
/* global mockApis */
'use strict';

describe('Controllers Tests ', function() {
  var $scope;
  var createController;
  var result = 'result';
  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _LogsResource_, $q) {
      $scope = _$rootScope_.$new();
      spyOn(_LogsResource_, 'changeLevel').
      and.callFake(function(par, callback) {
        callback();
        var deferred = $q.defer();
        deferred.resolve(result);
        return deferred.promise;
      });
      spyOn(_LogsResource_, 'findAll').and.callFake(function() {
        var deferred = $q.defer();
        deferred.resolve(result);
        return deferred.promise;
      });

      var locals = {
        '$scope': $scope,
        'LogsResource': _LogsResource_
      };
      createController = function() {
        return $controller('LogsController', locals);
      };
    });
  });
  describe('LogsController',function() {
     beforeEach(function() {
       createController();
     });
     it('should set $scope.loggers',function() {
       $scope.changeLevel('user',1);
       expect($scope.loggers.$$state.value).toEqual(result);
     });
   });
});
