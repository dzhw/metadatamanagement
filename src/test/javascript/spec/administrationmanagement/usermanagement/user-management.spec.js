'use strict';

describe('user-management api', function() {
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
  describe('basic test fo user-management', function() {
    it('should call $translate and $translatePartialLoader', function() {
      var config = $state.get('user-management');
      expect(config.url).toEqual('/user-management');
      expect(config.resolve.translatePartialLoader).toBeDefined();
      config.resolve.translatePartialLoader[2]($translate,
        $translatePartialLoader);
      expect($translate.refresh).toHaveBeenCalled();
      expect($translatePartialLoader.addPart).toHaveBeenCalled();
    });
  });
  describe('basic test fo user-management-detail', function() {
    it('should call $translate and $translatePartialLoader', function() {
      var config = $state.get('user-management-detail');
      expect(config.url).toEqual('/user-management/:login');
      expect(config.resolve.translatePartialLoader).toBeDefined();
      $state.go('user-management-detail');
      expect($translate.refresh).toHaveBeenCalled();
      expect($translatePartialLoader.addPart).toHaveBeenCalled();
    });
  });
});
