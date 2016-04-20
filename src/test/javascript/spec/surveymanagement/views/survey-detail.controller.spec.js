'use strict';

describe('Survey Detail Controller', function() {
    var $scope, $rootScope, MockEntity, createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('Survey');

        var locals = {
            '$scope': $scope,
            'entity': MockEntity ,
        };
        createController = function() {
            $injector.get('$controller')("SurveyDetailController", locals);
        };
    }));
    it('should set $scope.survey', function() {
      createController();
      expect($scope.survey).toBeDefined();
    });
});
