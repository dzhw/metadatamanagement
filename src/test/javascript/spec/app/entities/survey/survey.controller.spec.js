'use strict';

xdescribe('Controllers Tests ', function () {
  var $scope, Survey, createController;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_) {
      $scope = _$rootScope_.$new();
      Survey = {
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
        'Survey' : Survey
      };
      createController = function() {
        return $controller('SurveyController', locals);
      };
      spyOn(Survey, 'query').and.callThrough();
    });
   });
   describe('SurveyController',function(){
     beforeEach(function(){
         createController();
     });
     it('should call Survey.query',function(){
       $scope.loadAll();
       expect(Survey.query).toHaveBeenCalled();
     });
     it('should set page to 10',function(){
       $scope.loadPage(10);
       expect($scope.page).toEqual(10);
     });
     it('should call $scope.clear',function(){
       $scope.refresh();
       expect($scope.survey).toEqual({
         'title': null,
         'fieldPeriod': null,
         'fdzProjectName': null,
         'id': null
        });
     });
   });
});
