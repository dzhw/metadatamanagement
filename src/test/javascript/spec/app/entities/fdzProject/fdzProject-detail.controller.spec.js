'use strict';

describe('Controllers Tests ', function () {
  var $scope, createController, FdzProjectExportService, MockEntity;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _FdzProjectExportService_) {
      $scope = _$rootScope_.$new();
      FdzProjectExportService = {
        exportToODT:function(){

        }
      };
      var locals = {
        '$scope' : $scope,
        'entity': MockEntity ,
        'FdzProjectExportService': FdzProjectExportService,
      };
      createController = function() {
        return $controller('FdzProjectDetailController', locals);
      };
      spyOn(FdzProjectExportService, 'exportToODT').and.callThrough();
    });
   });
   describe('FdzProjectDetailController',function(){
     beforeEach(function(){
         createController();
     });
     it('$scope.isSaving should be false',function(){
       $scope.exportToODT();
       expect(FdzProjectExportService.exportToODT).toHaveBeenCalled();
     });
   });
});
