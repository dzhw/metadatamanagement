'use strict';

xdescribe('Factory Tests ', function () {
  var authInterceptor, localStorageService, response, $q, $scope;
    describe('authInterceptor', function() {
        beforeEach(inject(function($injector) {
            authInterceptor = $injector.get('authInterceptor');
            $q = $injector.get('$q');
            $scope = $injector.get('$rootScope').$new();
          localStorageService = (function() {
            return {
              get: function(key) {
                return {
                  'expires_at':12-12-3001
                };
              }
            };
          })();
          Object.defineProperty(window, 'localStorageService', { value: localStorageService, configurable: true, enumerable: true, writable: true });
          spyOn(localStorageService, 'get').and.callThrough()
        }));
        it('authInterceptor should be defined', function() {
          authInterceptor.request({});
          expect(authInterceptor).toBeDefined();
        });
      });
  });
