'use strict';

describe('Controllers Tests ', function() {

  beforeEach(mockApis);

  describe('ActivationController', function() {

    var $scope, $httpBackend, $q, MockAuth; // actual implementations
    var MockAuth, MockStateParams, MockState; // mocks
    var createController; // local utility function

    beforeEach(inject(function($injector) {
      $q = $injector.get('$q');
      $scope = $injector.get('$rootScope').$new();
      MockAuth = jasmine.createSpyObj('MockAuth', [
        'activateAccount'
      ]);
      MockStateParams = jasmine.createSpy('MockStateParams');
      MockStateParams.key = 'ABC123';
      MockState = jasmine.createSpy('MockState');
      MockState.current = {'name': 'activate'};
      var locals = {
        '$scope': $scope,
        '$state': MockState,
        '$stateParams': MockStateParams,
        'Auth': MockAuth
      };
      createController = function() {
        $injector.get('$controller')('ActivationController',
          locals);
      };
    }));

    it('calls Auth.activateAccount with the key from stateParams',
      function() {
        MockAuth.activateAccount.and.returnValue($q.resolve());
        $scope.$apply(createController);
        expect(MockAuth.activateAccount).toHaveBeenCalledWith({
          key: 'ABC123'
        });
      });

    it('should set set success to OK upon successful activation',
      function() {
        MockAuth.activateAccount.and.returnValue($q.resolve());
        $scope.$apply(createController);
        expect($scope.error).toBe(null);
        expect($scope.success).toEqual('OK');
      });

    it('should set set error to ERROR upon activation failure',
      function() {
        MockAuth.activateAccount.and.returnValue($q.reject());
        $scope.$apply(createController);
        expect($scope.error).toBe('ERROR');
        expect($scope.success).toEqual(null);
      });
  });
});
