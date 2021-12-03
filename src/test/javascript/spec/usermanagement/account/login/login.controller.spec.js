'use strict';

describe('Controllers Tests ', function() {
  beforeEach(mockSso);
  beforeEach(mockApis);
  describe('LoginController', function() {
    var $scope, $rootScope, $httpBackend, $q, MockAuth, event,
      MockState, MockTimeout, createController;
    beforeEach(inject(function($injector) {
      $q = $injector.get('$q');
      $rootScope = $injector.get('$rootScope');
      $scope = $injector.get('$rootScope').$new();
      MockAuth = jasmine.createSpyObj('MockAuth', ['login']);
      MockState = jasmine.createSpyObj('MockState', ['go']);
      MockTimeout = jasmine.createSpy('MockTimeout');
      event = jasmine.createSpyObj('event', ['preventDefault']);
      MockState.current = {'name': 'login'};
      var locals = {
        '$scope': $scope,
        'Auth': MockAuth,
        '$state': MockState,
        '$timeout': MockTimeout
      };
      createController = function() {
        $injector.get('$controller')('LoginController', locals);
      };
    }));
    beforeEach(function() {
      createController();
    });
    it('login should be a function', function() {
      expect($scope.login).toEqual(jasmine.any(Function));
    });
    it('should set authenticationError to true upon login failure',
      function() {
        MockAuth.login.and.returnValue($q.reject());
        $scope.login(event);
        $scope.$apply(createController);
        expect($scope.authenticationError).toBe(true);
      });
  });
});
