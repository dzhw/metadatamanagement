/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */

'use strict';

describe('Factory Tests ', function() {
  var AuthServiceProvider, Base64, localStorageService, $q, $scope;
  describe('AuthServiceProvider', function() {
    beforeEach(inject(function($injector) {
      AuthServiceProvider = $injector.get('AuthServiceProvider');
      $q = $injector.get('$q');
      Base64 = $injector.get('Base64');
      $scope = $injector.get('$rootScope').$new();
      localStorageService = $injector.get('localStorageService');
      spyOn(localStorageService, 'get').and.callThrough();

      var deferred = $q.defer();
      deferred.reject();
      spyOn(AuthServiceProvider, 'isLoggedInSso').and.returnValue(deferred.promise);
    }));
    it('AuthServerProvider.login( should be defined', function() {
      expect(AuthServiceProvider.login).toBeDefined();
    });
    /*it(' should make a POST request', function() {
      var credentials = {
        'username': 'admin',
        'password': 'pw'
      };
      var defer = $q.defer();
      defer.resolve(credentials);
      defer.promise.success = function(fn) {
        fn({
          expires_in: new Date()
        });
        defer.promise.then(null, fn);
        return defer.promise;
      };
      $httpMock.post.and.returnValue(defer.promise);
      AuthServerProvider.login(credentials);
      $scope.$digest();
      expect($httpMock.post).toHaveBeenCalled();
    });*/
    /*it('should call localStorageService.get', function() {
      AuthServiceProvider.getToken();
      expect(localStorageService.get).toHaveBeenCalled();
    });*/
    /*it('should call localStorageService.get', function() {
      var token = AuthServiceProvider.hasToken();
      expect(token).toBeDefined();
    });*/
  });
});
