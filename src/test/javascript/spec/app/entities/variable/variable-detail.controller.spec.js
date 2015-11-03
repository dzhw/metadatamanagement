'use strict';

describe('Variable Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockVariable, MockSurvey;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockVariable = jasmine.createSpy('MockVariable');
        MockSurvey = jasmine.createSpy('MockSurvey');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Variable': MockVariable,
            'Survey': MockSurvey
        };
        createController = function() {
            $injector.get('$controller')("VariableDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'metadatamanagementApp:variableUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
