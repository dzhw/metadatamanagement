'use strict';

xdescribe('Services Tests ', function() {

  describe('Auth', function() {
    var $httpBackend, spiedLocalStorageService, authService,
      spiedAuthServiceProvider;
    beforeEach(mockSso);
    beforeEach(inject(function($injector, localStorageService, Auth,
      AuthServiceProvider) {
      $httpBackend = $injector.get('$httpBackend');
      spiedLocalStorageService = localStorageService;
      authService = Auth;
      spiedAuthServiceProvider = AuthServiceProvider;
      //Request on app init
      $httpBackend.whenGET('scripts/app/main/main.html.tmpl').respond({});
      var globalJson = new RegExp('i18n\/.*\/global.json');
      var mainJson = new RegExp('i18n\/.*\/main.json');
      $httpBackend.whenGET(globalJson).respond({});
      $httpBackend.whenGET(mainJson).respond({});
    }));
    //make sure no expectations were missed in your tests.
    //(e.g. expectGET or expectPOST)
    afterEach(function() {
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });

    it(
      'should call backend on logout then call authServerProvider.logout',
      function() {
        //GIVEN
        //Set spy
        spyOn(spiedAuthServiceProvider, 'logout').and.callThrough();
        spyOn(spiedLocalStorageService, 'clearAll').and.callThrough();

        //WHEN
        authService.logout();
        //flush the backend to "execute" the request to do the expectedGET assertion.
        $httpBackend.flush();

        //THEN
        expect(spiedAuthServiceProvider.logout).toHaveBeenCalled();
        expect(spiedLocalStorageService.clearAll).toHaveBeenCalled();
      });

  });
});
