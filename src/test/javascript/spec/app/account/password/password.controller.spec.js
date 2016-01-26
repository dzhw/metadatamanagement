'use strict';

describe('Controllers Tests ', function() {
    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);

    describe('PasswordController', function() {

        var $scope, $httpBackend, $q, Principal;
        var MockAuth;
        var createController;

        beforeEach(inject(function($injector) {
            $scope = $injector.get('$rootScope').$new();
            $q = $injector.get('$q');
            $httpBackend = $injector.get('$httpBackend');

            MockAuth = jasmine.createSpyObj('MockAuth', ['changePassword']);
          Principal = {
              identity: function() {
              var deferred = $q.defer();
              deferred.resolve();
              return deferred.promise;
            }
          };
            var locals = {
                '$scope': $scope,
                'Auth': MockAuth,
                'Principal': Principal
            };
            createController = function() {
                $injector.get('$controller')('PasswordController', locals);
            };
            spyOn(Principal, 'identity').and.callThrough();
        }));

        it('should show error if passwords do not match', function() {
            //GIVEN
            createController();
            $scope.password = 'password1';
            $scope.confirmPassword = 'password2';
            //WHEN
            $scope.changePassword();
            //THEN
            expect($scope.doNotMatch).toBe('ERROR');
        });
        it('should call Auth.changePassword when passwords match', function() {
            //GIVEN
            MockAuth.changePassword.and.returnValue($q.resolve());
            createController();
            $scope.password = $scope.confirmPassword = 'myPassword';

            //WHEN
            $scope.$apply($scope.changePassword);

            //THEN
            expect(MockAuth.changePassword).toHaveBeenCalledWith('myPassword');
        });

        it('should set success to OK upon success', function() {
            //GIVEN
            MockAuth.changePassword.and.returnValue($q.resolve());
            createController();
            $scope.password = $scope.confirmPassword = 'myPassword';

            //WHEN
            $scope.$apply($scope.changePassword);

            //THEN
            expect($scope.error).toBeNull();
            expect($scope.success).toBe('OK');
        });

        it('should notify of error if change password fails', function() {
            //GIVEN
            MockAuth.changePassword.and.returnValue($q.reject());
            createController();
            $scope.password = $scope.confirmPassword = 'myPassword';

            //WHEN
            $scope.$apply($scope.changePassword);

            //THEN
            expect($scope.success).toBeNull();
            expect($scope.error).toBe('ERROR');
        });
        it('should call Principal.identity',function(){
          Principal.identity.and.returnValue($q.resolve());
          $scope.$apply(createController);
          expect(Principal.identity).toHaveBeenCalled();
        });
    });
});
