'use strict';

describe('Controllers Tests ', function () {
  var $scope, User, createController, Language;
  var editForm = {
    $setPristine: function(){

    },
    $setUntouched: function(){

    }
  };
  var header = function(par){
    return 'part1;part11,part2;part22';
  };
  var user = 'TestUser';
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
        query: function(par, callback) {
          callback(user,header);
          return {
             then: function(callback){
               return callback(['de','fr']);
             }
          };
        },
        update: function(user, callback){
          /*ToDO remove try and catch*/
          try{
          callback();
        }catch(e){

        }
          return {
             then: function(callback){
               return callback();
             }
          };
        },
        get: function(par,callback){
          callback();
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
      $scope.editForm = editForm;
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
       $scope.save();
       expect(User.save).toBeDefined();
     });
     it('should call scope.clear',function(){
       spyOn($scope,'clear').and.callThrough();
       $scope.refresh();
       expect($scope.clear).toHaveBeenCalled();
     });
     it('should set page to 10',function(){
      $scope.loadPage(10);
      expect($scope.page).toEqual(10);
     });
   });
});
