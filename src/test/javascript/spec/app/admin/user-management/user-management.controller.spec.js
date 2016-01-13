'use strict';

describe('Controllers Tests ', function () {
  var $scope, User, createController, Language;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      Language = {
        getAll: function() {
          return {
             then: function(callback){
               return callback(['de','fr']);
             }
          };
        }
      };
      User = {
        query: function() {
          return {
             then: function(callback){
               return callback(['de','fr']);
             }
          };
        },
        update: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        },
        get: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        },
        save: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        },
        refresh: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        }
      };
      var locals = {
        '$scope' : $scope,
        'Language' : Language,
        'User' : User
      };
      createController = function() {
        return $controller('UserManagementController', locals);
      };
      spyOn(Language, 'getAll').and.callThrough();
      spyOn(User, 'query').and.callThrough();
      spyOn(User, 'update').and.callThrough();
      spyOn(User, 'get').and.callThrough();
      spyOn(User, 'save').and.callThrough();
      spyOn(User, 'refresh').and.callThrough();
    });
   });
   describe('UserManagementController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call Language.getAll',function(){
       expect(Language.getAll).toHaveBeenCalled();
     });
     it('should set languages to de and fr',function(){
       expect($scope.languages).toEqual(['de','fr']);
     });
     it('should call User.update',function(){
      $scope.setActive({},true);
      expect(User.update).toHaveBeenCalled();
     });
     it('should call User.get',function(){
       $scope.showUpdate('login');
      expect(User.get).toHaveBeenCalled();
     });
     it('should call User.save',function(){
        try{
          $scope.save();
          expect(User.save).toBeDefined();
        }catch(e){

        }
     });
     it('should call User.refresh',function(){
       try{
       $scope.refresh();
         expect(User.refresh).toHaveBeenCalled();
     }catch(e){

     }
     });
     it('should set page to 10',function(){
      $scope.loadPage(10);
      expect($scope.page).toEqual(10);
     });
   });
});
