'use strict';

describe('Controllers Tests ', function() {
  beforeEach(mockApis);

  describe('PasswordController', function() {

    var $scope, $httpBackend, $q, MockPrincipal;
    var MockAuth;
    var createController;

    beforeEach(inject(function($injector) {
      $scope = $injector.get('$rootScope').$new();
      $q = $injector.get('$q');
      $httpBackend = $injector.get('$httpBackend');
      MockAuth = jasmine.createSpyObj('MockAuth', [
        'changePassword'
      ]);
      MockPrincipal = {
        identity: function() {
          var deferred = $q.defer();
          deferred.resolve();
          return deferred.promise;
        }
      };
      var locals = {
        '$scope': $scope,
        'Auth': MockAuth,
        'Principal': MockPrincipal
      };
      createController = function() {
        $injector.get('$controller')('PasswordController',
          locals);
      };
      spyOn(MockPrincipal, 'identity').and.callThrough();
    }));

    it('should show error if passwords do not match', function() {
      createController();
      $scope.password = 'password1';
      $scope.confirmPassword = 'password2';
      $scope.changePassword();
      expect($scope.doNotMatch).toBe('ERROR');
    });
    it('should call Auth.changePassword when passwords match', function() {
      MockAuth.changePassword.and.returnValue($q.resolve());
      createController();
      $scope.password = $scope.confirmPassword = 'myPassword';
      $scope.$apply($scope.changePassword);
      expect(MockAuth.changePassword).toHaveBeenCalledWith(
        'myPassword');
    });

    it('should set success to OK upon success', function() {
      MockAuth.changePassword.and.returnValue($q.resolve());
      createController();
      $scope.password = $scope.confirmPassword = 'myPassword';
      $scope.$apply($scope.changePassword);
      expect($scope.error).toBeNull();
      expect($scope.success).toBe('OK');
    });

    it('should notify of error if change password fails', function() {
      MockAuth.changePassword.and.returnValue($q.reject());
      createController();
      $scope.password = $scope.confirmPassword = 'myPassword';
      $scope.$apply($scope.changePassword);
      expect($scope.success).toBeNull();
      expect($scope.error).toBe('ERROR');
    });
    it('should call Principal.identity', function() {
      MockPrincipal.identity.and.returnValue($q.resolve());
      $scope.$apply(createController);
      expect(MockPrincipal.identity).toHaveBeenCalled();
    });
  });
});
