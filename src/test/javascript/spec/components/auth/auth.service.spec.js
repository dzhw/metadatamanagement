'use strict';

describe('Factory Tests ', function () {
  var Auth, $q, $scope, $rootScope, $state, Password, PasswordResetFinish, PasswordResetInit, Activate, deferred, Register, Account, Tracker, Principal, AuthServerProvider;
    describe('Auth', function() {
    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);
        beforeEach(inject(function($injector) {
            Auth = $injector.get('Auth');
            $q = $injector.get('$q');
            $rootScope = $injector.get('$rootScope')
            $scope = $rootScope.$new();
            AuthServerProvider = $injector.get('AuthServerProvider');
            Tracker = $injector.get('Tracker');
            Principal = $injector.get('Principal');
            Register = $injector.get('Register');
            Account = $injector.get('Account');
            $state = $injector.get('$state');
            Activate = $injector.get('Activate');
            Password = $injector.get('Password');
            PasswordResetInit = $injector.get('PasswordResetInit');
            PasswordResetFinish = $injector.get('PasswordResetFinish');
        }));
        beforeEach(function(){
           spyOn($state,'go').and.callThrough();
           spyOn(AuthServerProvider, 'login').and.callFake(function() {
             return {
               then: function(callback) { return callback({
                 'user':'admin'
               }); }
             };
           });
           spyOn(Tracker, 'sendActivity').and.callFake(function() {
             return {
               then: function(callback) { return callback({}); }
             };
           });
           spyOn(Principal, 'identity').and.callFake(function() {
             return {
               then: function(callback) { return callback({}); }
             };
           });
           spyOn(Register,'save').and.callFake(function(account,callback,error) {
             callback();
             error();
             return {
               then: function() { return callback({}); }
             };
           });
           spyOn(Account,'save').and.callFake(function(account,callback,error) {
             callback();
             error();
             return {
               then: function() { return callback({}); }
             };
           });
           spyOn(Activate,'get').and.callFake(function(key,callback,error) {
             callback();
             error();
             return {
               then: function() { return callback({}); }
             };
           });
           spyOn(Password,'save').and.callFake(function(newPassword,callback,error) {
             callback();
             error();
             return {
               then: function() { return callback({}); }
             };
           });
           spyOn(PasswordResetInit,'save').and.callFake(function(mail,callback,error) {
             callback();
             error();
             return {
               then: function() { return callback({}); }
             };
           });
           spyOn(PasswordResetFinish,'save').and.callFake(function(keyAndPassword,callback,error) {
             callback();
             error();
             return {
               then: function() { return callback({}); }
             };
           });
        });
        it('should call Principal.identity', function() {
            try{
              Auth.login({},function(){
                return {};
              });
            }catch(e){};
            expect(Principal.identity).toHaveBeenCalled();
          /*expect(function(){
          Auth.login();
        }).toThrow();*/

        });
        it('should set previousStateParams to undefined', function() {
          Auth.logout();
          expect($rootScope.previousStateParams).toEqual(undefined);
        });
        it('should redirect to home', function() {
          spyOn(Principal, 'isAuthenticated').and.callFake(function() {
            return true;
          });
          var toState = {
            'parent': 'account',
            'name': 'login',
          };
          $rootScope.toState = toState;
          Auth.authorize('');
          expect($state.go).toHaveBeenCalledWith('home');
        });
        it('should redirect to home', function() {
          spyOn(Principal, 'isAuthenticated').and.callFake(function() {
            return true;
          });
          var toState = {
            'parent': 'account',
            'name': 'register',
          };
          $rootScope.toState = toState;
          Auth.authorize('');
          expect($state.go).toHaveBeenCalledWith('home');
        });
        it('should redirect to accessdenied', function() {
          spyOn(Principal, 'isAuthenticated').and.callFake(function() {
            return true;
          });
          var toState = {
            data: {
              authorities:['ADMIN']
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
            parent: 'home',
            name: 'home',
            data: {
              authorities:['ADMIN']
            }
          };
          $rootScope.toState = toState;
          $rootScope.toStateParams = {
            lang:'de'
          }
          Auth.authorize();
          expect($state.go).toHaveBeenCalledWith('login',{'lang': 'de'});
        });
        it('should call Register.save', function() {
          Auth.createAccount('');
          expect(Register.save).toHaveBeenCalled();
        });
        it('should call Account.save', function() {
          Auth.updateAccount('');
          expect(Account.save).toHaveBeenCalled();
        });
        it('should call Activate.get', function() {
          Auth.activateAccount('');
          expect(Activate.get).toHaveBeenCalled();
        });
        it('should call Password.save', function() {
          Auth.changePassword('');
          expect(Password.save).toHaveBeenCalled();
        });
        it('should call PasswordResetInit.save', function() {
          Auth.resetPasswordInit('');
          expect(PasswordResetInit.save).toHaveBeenCalled();
        });
        it('should call PasswordResetFinish.save', function() {
          Auth.resetPasswordFinish('');
          expect(PasswordResetFinish.save).toHaveBeenCalled();
        });
      });
    });
