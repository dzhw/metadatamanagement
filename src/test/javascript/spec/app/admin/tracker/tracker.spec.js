'use strict';

describe('tracker api', function() {

  describe('basic test', function() {

    it('should call configuration', inject(function(_Tracker_,
      $translate, $translatePartialLoader, $httpBackend, $state) {
      var globalJson = new RegExp('i18n\/.*\/global.json')
      var mainJson = new RegExp('i18n\/.*\/main.json');
      var Tracker = _Tracker_;
      $httpBackend.whenGET(globalJson).respond({});
      $httpBackend.whenGET(mainJson).respond({});
      $httpBackend.expectGET(/api\/account\?cacheBuster=\d+/).respond(
        200, '');
      spyOn($translate, 'refresh').and.callThrough();
      spyOn($translatePartialLoader, 'addPart').and.callThrough();
      spyOn(Tracker, 'subscribe').and.callThrough();
      spyOn(Tracker, 'unsubscribe').and.callThrough();
      var config = $state.get('tracker');
      expect(config.url).toEqual('/tracker');
      expect(config.resolve.mainTranslatePartialLoader).toBeDefined();
      $state.go('tracker');
      config.onEnter(Tracker);
      config.onExit(Tracker);
      expect($translate.refresh).toHaveBeenCalled();
      expect($translatePartialLoader.addPart).toHaveBeenCalled();
      expect(Tracker.subscribe).toHaveBeenCalled();
      expect(Tracker.unsubscribe).toHaveBeenCalled();
    }));
  });
});
