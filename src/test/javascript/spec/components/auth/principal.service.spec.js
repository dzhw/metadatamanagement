'use strict';

describe('Factory Tests ', function() {
  var Principal, Account, $q, $scope;
  describe('Principal', function() {
    //beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);
    beforeEach(inject(function($injector) {
      Principal = $injector.get('Principal');
      $q = $injector.get('$q');
      $scope = $injector.get('$rootScope').$new();
      //Account = $injector.get('Account');
      Account = {
        get: function() {
          var deferred = $q.defer();
          deferred.resolve({
            account: 'account'
          });
          return deferred.promise;
        }
      };
      spyOn(Account, 'get').and.callThrough();
    }));
    it('should return false', function() {
      var retVal = Principal.hasAuthority(true);
      expect(retVal.$$state.value).toEqual(false);
    });
    it('should return false', function() {
      spyOn(Principal, 'identity').and.callFake(function() {
        var deferred = $q.defer();
        deferred.resolve();
        return deferred.promise;
      });
      Principal.identity.and.returnValue($q.resolve());
      Principal.authenticate();
      var retVal = Principal.hasAuthority(true);

    });
    /*ToDO*/
    it('should ', function() {
      Principal.authenticate({
        authorities: ['ADMIN']
      });
      Principal.hasAnyAuthority(['ADMIN']);
    });
    /*ToDO*/
    it('should ', function() {
      Principal.authenticate({
        authorities: []
      });
      Principal.hasAnyAuthority(['ADMIN']);
    });
    /*ToDO*/
    it('should ', function() {
      Principal.identity(true);
    });
  });
});
