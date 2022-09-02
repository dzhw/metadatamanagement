'use strict';

describe('Factory Tests ', function() {
  var Principal, Account, $q, $scope, $httpBackend, AuthServiceProvider;
  describe('Principal', function() {

    beforeEach(mockSso);
    beforeEach(inject(function($injector) {
      Principal = $injector.get('Principal');
      $httpBackend = $injector.get('$httpBackend');
      $q = $injector.get('$q');
      $scope = $injector.get('$rootScope').$new();
      AuthServiceProvider = $injector.get('AuthServiceProvider');
      $httpBackend.whenGET(/i18n\/de\/.+\.json/).respond({});

    }));
    it('should return false', function() {
      var retVal = Principal.hasAuthority(true);
      expect(retVal).toEqual(false);
    });
    it('should have authority', function() {
      spyOn(AuthServiceProvider, 'accessTokenInfo').and.callFake(function() {
        return {scope: ['admin']};
      });
      Principal.hasAnyAuthority(['ADMIN']);
    });
  });
});
