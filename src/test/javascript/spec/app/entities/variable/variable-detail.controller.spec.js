'use strict';

describe('VariableDetailController', function() {
    var $scope, $rootScope, MockEntity, VariableExportService, createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');

        VariableExportService = {
          exportToODT: function(){
            return {
               then: function(callback){
                 return callback();
               }
            };
          }
        };


        var locals = {
            '$scope': $scope,
            'entity': MockEntity ,
            'VariableExportService': VariableExportService
        };
        createController = function() {
            $injector.get('$controller')("VariableDetailController", locals);
        };
        spyOn(VariableExportService, 'exportToODT').and.callThrough();
    }));
    describe('', function() {
        it('should call Survey.get', function() {
            createController();
            $scope.exportToODT();
            expect(VariableExportService.exportToODT).toHaveBeenCalled();
        });
    });
});
