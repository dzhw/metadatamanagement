'use strict';

describe('Factory Tests ', function () {
  var AuthServerProvider, Base64, localStorageService, $httpMock, $q, $scope;
    describe('AuthServerProvider', function() {
      beforeEach(module(function ($provide) {
          $httpMock = jasmine.createSpyObj('$http',['post']);
          $provide.value('$http', $httpMock);
    }));
        beforeEach(inject(function($injector) {
            AuthServerProvider = $injector.get('AuthServerProvider');
            $q = $injector.get('$q');
            Base64 = $injector.get('Base64');
            $scope = $injector.get('$rootScope').$new();
            localStorageService = $injector.get('localStorageService');
            spyOn(localStorageService,'get').and.callThrough();
        }));
        it('AuthServerProvider.login( should be defined', function() {
          expect(AuthServerProvider.login).toBeDefined();
        });
        it(' should make a POST request', function() {
          var credentials = {
            'username': 'admin',
            'password': 'pw'
          };
          var defer = $q.defer();
          defer.resolve(credentials);
          defer.promise.success = function (fn) {
            defer.promise.then(null, fn);
            return defer.promise;
          };
          $httpMock.post.and.returnValue(defer.promise);
          //$httpMock.success.and.returnValue(defer.promise);
          AuthServerProvider.login(credentials);
          $scope.$digest();
          expect($httpMock.post).toHaveBeenCalled();
        });
        it('should call localStorageService.get', function() {
          AuthServerProvider.getToken();
          expect(localStorageService.get).toHaveBeenCalled();
        });
        it('should call localStorageService.get', function() {
          var token = AuthServerProvider.hasValidToken();
          expect(token).toBeDefined();
        });
      });
    });
