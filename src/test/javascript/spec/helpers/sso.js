'use strict';

function mockSso() {
  inject(function ($httpBackend, $q, AuthServiceProvider) {
    spyOn(AuthServiceProvider, 'isLoggedIn').and.callFake(function () {
      var deferred = $q.defer();
      deferred.reject(false);
      return deferred.promise;
    });
    $httpBackend.whenGET(/user\/login\?_format=json$/).respond(406);
  });
}
