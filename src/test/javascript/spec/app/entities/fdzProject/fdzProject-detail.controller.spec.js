'use strict';

xdescribe('FdzProject Detail Controller', function() {
    var $scope, $rootScope, FdzProject;
    var MockEntity, MockFdzProject;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        //MockFdzProject = jasmine.createSpy('MockFdzProject');
        FdzProject = {
          get: function(){
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
            $injector.get('$controller')("FdzProjectDetailController", locals);
        };
        spyOn(FdzProject, 'get').and.callThrough();
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'metadatamanagementApp:fdzProjectUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
    describe('', function() {
        it('should call FdzProject.get', function() {
            createController();
            $scope.load('r');
            expect(FdzProject.get).toHaveBeenCalled();
        });
    });
});
