'use strict';

xdescribe('Survey Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, Survey;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');

        Survey = {
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
            'Survey': Survey
        };
        createController = function() {
            $injector.get('$controller')("SurveyDetailController", locals);
        };
        spyOn(Survey, 'get').and.callThrough();
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'metadatamanagementApp:surveyUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
    describe('', function() {
        it('should call Survey.get', function() {
            createController();
            $scope.load('r');
            expect(Survey.get).toHaveBeenCalled();
        });
    });
});
