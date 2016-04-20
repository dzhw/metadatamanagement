'use strict';

describe('Controllers Tests ', function () {
  var $scope, Principal, createController;

  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      Principal = {
        identity: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        }
      };
      var locals = {
        '$scope' : $scope,
        'Principal' : Principal
      };
      createController = function() {
        return $controller('DisclosureController', locals);
      };
      spyOn(Principal, 'identity').and.callThrough();
    });
   });
   describe('DisclosureController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call Principal.identity',function(){
         expect(Principal.identity).toHaveBeenCalled();
     });
   });
});
