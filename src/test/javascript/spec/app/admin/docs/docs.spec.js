'use strict';

describe('audits api', function () {

  describe('basic test', function() {
    it('should call $translate and $translatePartialLoader', inject(function($translate, $httpBackend, $state) {
      var globalJson = new RegExp('i18n\/.*\/global.json')
      var mainJson = new RegExp('i18n\/.*\/main.json');
      $httpBackend.whenGET(globalJson).respond({});
      $httpBackend.whenGET(mainJson).respond({});
      $httpBackend.expectGET(/api\/account\?cacheBuster=\d+/).respond(200, '');
      spyOn($translate,'refresh').and.callThrough();
      var config = $state.get('docs');
      expect(config.url).toEqual('/docs');
      expect(config.resolve.translatePartialLoader).toBeDefined;
      config.resolve.translatePartialLoader[1]($translate);
      expect($translate.refresh).toHaveBeenCalled();
    }));
  });
});
