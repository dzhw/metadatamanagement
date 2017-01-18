'use strict';

describe('Factory Tests ', function() {
  var Principal, Account, $q, $scope, $httpBackend;
  describe('Principal', function() {

    beforeEach(inject(function($injector) {
      Principal = $injector.get('Principal');
      $httpBackend = $injector.get('$httpBackend');
      $q = $injector.get('$q');
      $scope = $injector.get('$rootScope').$new();
      $httpBackend.whenGET(/i18n\/de\/.+\.json/).respond({});
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
      expect(retVal).toEqual(false);
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
