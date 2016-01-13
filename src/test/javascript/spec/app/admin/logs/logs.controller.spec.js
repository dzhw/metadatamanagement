'use strict';

describe('Controllers Tests ', function () {
  var $scope, LogsService, createController;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _LogsService_) {
      $scope = _$rootScope_.$new();
      LogsService = {
        findAll: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        },
        changeLevel: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        }
      };
      var locals = {
        '$scope' : $scope,
        'LogsService' : LogsService
      };
      createController = function() {
        return $controller('LogsController', locals);
      };
      spyOn(LogsService, 'findAll').and.callThrough();
      spyOn(LogsService, 'changeLevel').and.callThrough();
    });
   });
   describe('LogsController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call LogsService.get',function(){
         expect(LogsService.findAll).toHaveBeenCalled();
     });
     it('should call LogsService.changeLevel',function(){
        $scope.changeLevel('user',1);
         expect(LogsService.changeLevel).toHaveBeenCalled();
     });
   });
});
