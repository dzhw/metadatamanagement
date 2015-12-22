'use strict';

describe('FdzProject Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockFdzProject;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockFdzProject = jasmine.createSpy('MockFdzProject');


        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'FdzProject': MockFdzProject
        };
        createController = function() {
            $injector.get('$controller')("FdzProjectDetailController", locals);
        };
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
});
