/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global mockApis */
/* global spyOn */
'use strict';

describe('Controllers Tests ', function() {
  var $scope;
  var UserResource;
  var createController;

  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      UserResource = {
        get: function(par, callback) {
          return callback('TestUser');

        }
      };
      var locals = {
        '$scope': $scope,
        'UserResource': UserResource
      };
      createController = function() {
        return $controller('UserManagementDetailController', locals);
      };
      spyOn(UserResource, 'get').and.callThrough();
    });
  });
  describe('UserManagementDetailController',function() {
     beforeEach(function() {
       createController();
     });
     it('should call User.get',function() {
       expect(UserResource.get).toHaveBeenCalled();
     });
     it('should set user',function() {
       expect($scope.user).toBe('TestUser');
     });
   });
});
