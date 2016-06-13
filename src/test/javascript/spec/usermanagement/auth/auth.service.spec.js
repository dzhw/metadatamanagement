/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global mockApis */
/* global spyOn */

'use strict';

describe('Factory Tests ', function() {
  var Auth;
  var $q;
  var $scope;
  var $rootScope;
  var $state;
  var PasswordResource;
  var PasswordResetFinishResource;
  var PasswordResetInitResource;
  var ActivateResource;
  var RegisterResource;
  var AccountResource;
  var Principal;
  var AuthServerProvider;
  describe('Auth', function() {
    beforeEach(mockApis);
    beforeEach(inject(function($injector) {
      Auth = $injector.get('Auth');
      $q = $injector.get('$q');
      $rootScope = $injector.get('$rootScope');
      $scope = $rootScope.$new();
      AuthServerProvider = $injector.get('AuthServerProvider');
      Principal = $injector.get('Principal');
      RegisterResource = $injector.get('RegisterResource');
      AccountResource = $injector.get('AccountResource');
      $state = $injector.get('$state');
      ActivateResource = $injector.get('ActivateResource');
      PasswordResource = $injector.get('PasswordResource');
      PasswordResetInitResource = $injector.get(
        'PasswordResetInitResource');
      PasswordResetFinishResource = $injector
        .get('PasswordResetFinishResource');
    }));
    beforeEach(function() {
      spyOn($state, 'go').and.callThrough();
      spyOn(AuthServerProvider, 'login').and.callFake(function() {
        return {
          then: function(callback) {
            return callback({
              'user': 'admin'
            });
          }
        };
      });
      spyOn(Principal, 'identity').and.callFake(function() {
        return {
          then: function(callback) {
            return callback({});
          }
        };
      });
      spyOn(RegisterResource, 'save').and.callFake(function(account,
        callback, error) {
        callback();
        error();
        return {
          then: function() {
            return callback({});
          }
        };
      });
      spyOn(AccountResource, 'save').and.callFake(function(account,
        callback, error) {
        callback();
        error();
        return {
          then: function() {
            return callback({});
          }
        };
      });
      spyOn(ActivateResource, 'get').and.callFake(function(key,
        callback,
        error) {
        callback();
        error();
        return {
          then: function() {
            return callback({});
          }
        };
      });
      spyOn(PasswordResource, 'save').and.callFake(function(
        newPassword,
        callback, error) {
        callback();
        error();
        return {
          then: function() {
            return callback({});
          }
        };
      });
      spyOn(PasswordResetInitResource, 'save').and.callFake(
        function(mail,
          callback, error) {
          callback();
          error();
          return {
            then: function() {
              return callback({});
            }
          };
        });
      spyOn(PasswordResetFinishResource, 'save').and.callFake(
        function(
          keyAndPassword, callback, error) {
          callback();
          error();
          return {
            then: function() {
              return callback({});
            }
          };
        });
    });
    it('should call Principal.identity', function() {
      try {
        Auth.login({}, function() {
          return {};
        });
      } catch (e) {}
      expect(Principal.identity).toHaveBeenCalled();
      /*expect(function(){
          Auth.login();
        }).toThrow();*/

    });
    it('should set previousStateParams to undefined', function() {
      Auth.logout();
      expect($rootScope.previousStateParams).toEqual(undefined);
    });
    it('should redirect to search', function() {
      spyOn(Principal, 'isAuthenticated').and.callFake(function() {
        return true;
      });
      var toState = {
        'parent': 'account',
        'name': 'login',
      };
      $rootScope.toState = toState;
      Auth.authorize('');
      expect($state.go).toHaveBeenCalledWith('search');
    });
    it('should redirect to search', function() {
      spyOn(Principal, 'isAuthenticated').and.callFake(function() {
        return true;
      });
      var toState = {
        'parent': 'account',
        'name': 'register',
      };
      $rootScope.toState = toState;
      Auth.authorize('');
      expect($state.go).toHaveBeenCalledWith('search');
    });
    it('should redirect to accessdenied', function() {
      spyOn(Principal, 'isAuthenticated').and.callFake(function() {
        return true;
      });
      var toState = {
        data: {
          authorities: ['ADMIN']
        }
      };
      $rootScope.toState = toState;
      Auth.authorize('');
      expect($state.go).toHaveBeenCalledWith('accessdenied');
    });
    it('should redirect to login', function() {
      spyOn(Principal, 'isAuthenticated').and.callFake(function() {
        return false;
      });
      var toState = {
        parent: 'search',
        name: 'search',
        data: {
          authorities: ['ADMIN']
        }
      };
      $rootScope.toState = toState;
      $rootScope.toStateParams = {
        lang: 'de'
      };
      Auth.authorize();
      expect($state.go).toHaveBeenCalledWith('login', {
        'lang': 'de'
      });
    });
    it('should call Register.save', function() {
      Auth.createAccount('');
      expect(RegisterResource.save).toHaveBeenCalled();
    });
    it('should call Account.save', function() {
      Auth.updateAccount('');
      expect(AccountResource.save).toHaveBeenCalled();
    });
    it('should call Activate.get', function() {
      Auth.activateAccount('');
      expect(ActivateResource.get).toHaveBeenCalled();
    });
    it('should call Password.save', function() {
      Auth.changePassword('');
      expect(PasswordResource.save).toHaveBeenCalled();
    });
    it('should call PasswordResetInit.save', function() {
      Auth.resetPasswordInit('');
      expect(PasswordResetInitResource.save).toHaveBeenCalled();
    });
    it('should call PasswordResetFinish.save', function() {
      Auth.resetPasswordFinish('');
      expect(PasswordResetFinishResource.save).toHaveBeenCalled();
    });
  });
});
