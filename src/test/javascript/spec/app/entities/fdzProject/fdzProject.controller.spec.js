'use strict';

xdescribe('Controllers Tests ', function () {
  var $scope, FdzProject, createController;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      FdzProject = {
        query: function(){
          return {
             then: function(callback){
               return callback();
             }
          };
        }
      };
      var locals = {
        '$scope' : $scope,
        'FdzProject' : FdzProject
      };
      createController = function() {
        return $controller('FdzProjectController', locals);
      };
      spyOn(FdzProject, 'query').and.callThrough();
    });
   });
   describe('FdzProjectController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call FdzProject.query',function(){
       $scope.loadAll();
       expect(FdzProject.query).toHaveBeenCalled();
     });
     it('should set page to 10',function(){
       $scope.loadPage(10);
       expect($scope.page).toEqual(10);
     });
     it('should call $scope.clear',function(){
       $scope.refresh();
       expect($scope.fdzProject).toEqual({
          'name': null,
          'sufDoi': null,
          'cufDoi': null
        });
     });
   });
});
