'use strict';

describe('Controllers Tests ', function () {
  var $scope, User, createController;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      User = {
        get: function(par,callback){
          callback('TestUser');
          return {
             then: function(callback){
               return callback();
             }
          };
        }
      };
      var locals = {
        '$scope' : $scope,
        'User' : User
      };
      createController = function() {
        return $controller('UserManagementDetailController', locals);
      };
      spyOn(User, 'get').and.callThrough();
    });
   });
   describe('UserManagementDetailController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call User.get',function(){
         expect(User.get).toHaveBeenCalled();
     });
     it('should set user',function(){
         expect($scope.user).toBe('TestUser');
     });
   });
});
