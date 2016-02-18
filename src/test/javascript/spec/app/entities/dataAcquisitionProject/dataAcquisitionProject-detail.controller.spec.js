'use strict';

describe('Controllers Tests ', function () {
  var $scope, createController, DataAcquisitionProjectExportService, MockEntity;

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _DataAcquisitionProjectExportService_) {
      $scope = _$rootScope_.$new();
      DataAcquisitionProjectExportService = {
        exportToODT:function(){

        }
      };
      var locals = {
        '$scope' : $scope,
        'entity': MockEntity ,
        'DataAcquisitionProjectExportService': DataAcquisitionProjectExportService,
      };
      createController = function() {
        return $controller('DataAcquisitionProjectDetailController', locals);
      };
      spyOn(DataAcquisitionProjectExportService, 'exportToODT').and.callThrough();
    });
   });
   describe('DataAcquisitionProjectDetailController',function(){
     beforeEach(function(){
         createController();
     });
     it('$scope.isSaving should be false',function(){
       $scope.exportToODT();
       expect(DataAcquisitionProjectExportService.exportToODT).toHaveBeenCalled();
     });
   });
});
