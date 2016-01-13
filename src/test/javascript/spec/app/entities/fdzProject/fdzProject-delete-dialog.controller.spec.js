'use strict';

xdescribe('FdzProject Detail Controller', function() {
    var $scope, $rootScope, FdzProject;
    var MockEntity;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        FdzProject = {
          delete: function(){
            return {
               then: function(callback){
                 return callback();
               }
            };
          }
        };

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'FdzProject': FdzProject
        };
        createController = function() {
            $injector.get('$controller')("FdzProjectDeleteController", locals);
        };
        spyOn(FdzProject, 'delete').and.callThrough();
    }));
    describe('FdzProjectDeleteController', function() {
        it('should call FdzProject.delete', function() {
            createController();
           $scope.confirmDelete();
            expect(FdzProject.delete).toHaveBeenCalled();
        });
    });
});
