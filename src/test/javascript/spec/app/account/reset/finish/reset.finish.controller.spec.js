'use strict';

describe('Controller Tests', function() {

    beforeEach(mockApis);

    describe('ResetFinishController', function() {

        var $scope, $q; // actual implementations
        var MockStateParams, MockTimeout, MockAuth; // mocks
        var createController; // local utility function

        beforeEach(inject(function($injector) {
            $q = $injector.get('$q');
            $scope = $injector.get('$rootScope').$new();
            MockStateParams = jasmine.createSpy('MockStateParams');
            MockTimeout = jasmine.createSpy('MockTimeout');
            MockAuth = jasmine.createSpyObj('MockAuth', ['resetPasswordFinish']);
            MockStateParams.key = 'ABCDEFG';
            var locals = {
                '$scope': $scope,
                '$stateParams': MockStateParams,
                '$timeout': MockTimeout,
                'Auth': MockAuth
            };
            createController = function() {
                return $injector.get('$controller')('ResetFinishController', locals);
            };
        }));

        beforeEach(function(){
          createController();
        });
        it('should define its initial state', function() {
            MockStateParams.key = 'XYZPDQ';
            $scope.$apply(createController);
            expect($scope.keyMissing).toBeFalsy();
            expect($scope.doNotMatch).toBeNull();
            expect($scope.resetAccount).toEqual({});
        });

        it('registers a timeout handler set set focus', function() {
            var MockAngular = jasmine.createSpyObj('MockAngular', ['element']);
            var MockElement = jasmine.createSpyObj('MockElement', ['focus']);
            MockAngular.element.and.returnValue(MockElement);
            MockTimeout.and.callFake(function(callback) {
                withMockedAngular(MockAngular, callback)();
            });
            $scope.$apply(createController);
            expect(MockTimeout).toHaveBeenCalledWith(jasmine.any(Function));
            expect(MockAngular.element).toHaveBeenCalledWith('[ng-model="resetAccount.password"]');
            expect(MockElement.focus).toHaveBeenCalled();
        });
        it('doNotMatch should be ERROR', function() {
            $scope.resetAccount.password ='fakePassword';
            $scope.confirmPassword ='fakePassword1';
            $scope.$apply(createController);
            $scope.finishReset();
            expect($scope.doNotMatch).toBe('ERROR');
        });
        it('shold call resetPasswordFinish with params', function() {
            MockAuth.resetPasswordFinish.and.returnValue($q.resolve());
            $scope.resetAccount.password ='fakePassword';
            $scope.confirmPassword ='fakePassword';
            $scope.finishReset();
            $scope.$apply(createController);

            expect(MockAuth.resetPasswordFinish).toHaveBeenCalledWith({
                key: 'ABCDEFG',
                newPassword: 'fakePassword'
            });
        });
        it('should set success and error values', function() {
            MockAuth.resetPasswordFinish.and.returnValue($q.reject());
            $scope.resetAccount.password ='fakePassword';
            $scope.confirmPassword ='fakePassword';
            $scope.finishReset();
            $scope.$apply(createController);
            expect($scope.success).toBe(null);
            expect($scope.error).toBe('ERROR');
        });

    });
});
