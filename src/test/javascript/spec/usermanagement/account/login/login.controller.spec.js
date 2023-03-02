'use strict';

describe('Controllers Tests ', function() {
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
      $scope.username = 'fakeAdmin';
      $scope.password = 'fakeAdmin';
      $scope.rememberMe = true;
    });
    it('should set remember Me', function() {
      expect($scope.rememberMe).toBeTruthy();
    });
    it('registers a timeout handler for focus', function() {
      var MockAngular = jasmine.createSpyObj('MockAngular', [
        'element'
      ]);
      var MockElement = jasmine.createSpyObj('MockElement', [
        'focus'
      ]);
      MockAngular.element.and.returnValue(MockElement);
      MockTimeout.and.callFake(function(callback) {
        withMockedAngular(MockAngular, callback)();
      });
      $scope.$apply(createController);
      expect(MockTimeout).toHaveBeenCalledWith(jasmine.any(Function));
      expect(MockAngular.element).toHaveBeenCalledWith(
        '[ng-model="username"]');
      expect(MockElement.focus).toHaveBeenCalled();
    });
    it('login should be a function', function() {
      expect($scope.login).toEqual(jasmine.any(Function));
    });
    it('should call Auth.login with params', function() {
      MockAuth.login.and.returnValue($q.resolve());
      $scope.login(event);
      expect(MockAuth.login).toHaveBeenCalledWith({
        username: 'fakeAdmin',
        password: 'fakeAdmin',
        rememberMe: true
      });
    });
    xit('should set authenticationError to true upon login failure',
      function() {
        // todo: refactor test so promises do work properly
        MockAuth.login.and.returnValue($q.reject());
        $scope.login(event);
        $scope.$apply(createController);
        expect($scope.authenticationError).toBe(true);
      });
    xit('should set authenticationError false upon login success',
      function() {
        // todo: refactor test so promises do work properly
        MockAuth.login.and.returnValue($q.resolve());
        $scope.login(event);
        $scope.$apply(createController);
        expect($scope.authenticationError).toBe(false);
      });
  });
});
