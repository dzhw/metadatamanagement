'use strict';

describe('Variable Detail Controller', function() {
    var $scope, $rootScope, MockEntity, createController, Language;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        Language = $injector.get('Language');
        $scope = $rootScope.$new();
        MockEntity = {
          $promise: {
            then: function() {}            
          }
        };

        var locals = {
            '$scope': $scope,
            'entity': MockEntity ,
            'Language' : Language
        };
        createController = function() {
            $injector.get('$controller')('VariableDetailController', locals);
        };
    }));
    it('should set $scope.variable', function() {
      createController();
      expect($scope.variable).toBeDefined();
    });
});
