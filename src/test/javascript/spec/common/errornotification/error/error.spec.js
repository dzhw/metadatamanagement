'use strict';

xdescribe('survey api', function() {
  var $translate, $translatePartialLoader, $state;
  beforeEach(inject(function(_$translate_, _$translatePartialLoader_,
    _$httpBackend_, _$state_) {
    $translate = _$translate_;
    $translatePartialLoader = _$translatePartialLoader_;
    $state = _$state_;
    var globalJson = new RegExp('i18n\/.*\/global.json')
    var mainJson = new RegExp('i18n\/.*\/main.json');
    _$httpBackend_.whenGET(globalJson).respond({});
    _$httpBackend_.whenGET(mainJson).respond({});
    _$httpBackend_.expectGET(/api\/account\?cacheBuster=\d+/).respond(
      200, '');
    spyOn($translate, 'refresh').and.callThrough();
    spyOn($translatePartialLoader, 'addPart').and.callThrough();
  }));
  describe('basic test accessdenied api', function() {
    it('should call $translate and $translatePartialLoader', function() {
      var config = $state.get('accessdenied');
      expect(config.url).toEqual('/accessdenied');
      expect(config.resolve.mainTranslatePartialLoader).toBeDefined();
      $state.go('accessdenied');
      expect($translate.refresh).toHaveBeenCalled();
      expect($translatePartialLoader.addPart).toHaveBeenCalled();
    });
  });
  describe('basic test error api', function() {
    it('should call $translate and $translatePartialLoader', function() {
      var config = $state.get('error');
      expect(config.url).toEqual('/error');
      expect(config.resolve.mainTranslatePartialLoader).toBeDefined();
      $state.go('error');
      expect($translate.refresh).toHaveBeenCalled();
      expect($translatePartialLoader.addPart).toHaveBeenCalled();
    });
  });
});
