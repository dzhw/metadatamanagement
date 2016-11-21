'use strict';

describe('Factory Tests ', function() {
  var LanguageService, $q, $translate, tmhDynamicLocale, $location,
    deferred,
    $scope;
  describe('LanguageService', function() {
    beforeEach(inject(function($injector) {
      LanguageService = $injector.get('LanguageService');
      $q = $injector.get('$q');
      $translate = $injector.get('$translate');
      $location = $injector.get('$location');
      deferred = $q.defer();
      $scope = $injector.get('$rootScope').$new();
      tmhDynamicLocale = {
        set: function() {
          return {
            then: function(callback) {
              return callback();
            }
          };
        }
      };
      spyOn(tmhDynamicLocale, 'set').and.callThrough();
    }));
    it('LanguageService should be defined', function() {
      expect(LanguageService).toBeDefined();
    });
    it('LanguageService should be defined', function() {
      var ret = LanguageService.getAll();
      expect(ret).toBeDefined();
    });
    it('LanguageService should be defined', function() {
      var ret = LanguageService.getAll();
      expect(ret).toBeDefined();
    });
  });
});
