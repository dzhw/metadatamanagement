'use strict';

function mockSso() {
  var $httpBackend, $q, AuthServiceProvider;
  inject(function (_$httpBackend_, _$q_, _AuthServiceProvider_) {
    $httpBackend = _$httpBackend_;
    $q = _$q_;
    AuthServiceProvider = _AuthServiceProvider_;

    spyOn(AuthServiceProvider, 'isLoggedIn').and.callFake(function () {
      var deferred = $q.defer();
      deferred.reject(false);
      return deferred.promise;
    });
    $httpBackend.expectGET(/user\/login\?_format=json$/).respond(406);
  });
}
