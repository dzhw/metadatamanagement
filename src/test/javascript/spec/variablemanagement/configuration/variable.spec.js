/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */

'use strict';

describe('survey api', function() {
  var $translate;
  var $translatePartialLoader;
  var $state;
  beforeEach(inject(function(_$translate_, _$translatePartialLoader_,
    _$httpBackend_, _$state_) {
    $translate = _$translate_;
    $translatePartialLoader = _$translatePartialLoader_;
    $state = _$state_;
    var globalJson = new RegExp('i18n\/.*\/global.json');
    var mainJson = new RegExp('i18n\/.*\/main.json');
    _$httpBackend_.whenGET(globalJson).respond({});
    _$httpBackend_.whenGET(mainJson).respond({});
    _$httpBackend_.expectGET(/api\/account\?cacheBuster=\d+/).respond(
      200, '');
    spyOn($translate, 'refresh').and.callThrough();
    spyOn($translatePartialLoader, 'addPart').and.callThrough();
  }));
  describe('basic test variable api', function() {
    it('should call $translate and $translatePartialLoader', function() {
      var config = $state.get('variable');
      expect(config.url).toEqual('/variables?{page, query}');
      expect(config.resolve.translatePartialLoader).toBeDefined();
      $state.go('variable');
      expect($translate.refresh).toHaveBeenCalled();
      expect($translatePartialLoader.addPart).toHaveBeenCalled();
    });
  });
  describe('basic test fo variable.detail', function() {
    it('should call $translate, $translatePartialLoader and variable',
      inject(function(_VariableResource_) {
        var config = $state.get('variable.detail');
        var VariableResource = _VariableResource_;
        spyOn(VariableResource, 'get').and.callThrough();
        expect(config.url).toEqual('/variables/{id}');
        expect(config.resolve.translatePartialLoader).toBeDefined();
        expect(config.resolve.entity).toBeDefined();
        $state.go('variable.detail');
        expect($translate.refresh).toHaveBeenCalled();
        expect($translatePartialLoader.addPart).toHaveBeenCalled();
        expect(VariableResource.get).toHaveBeenCalled();
      }));
  });
  describe('variable.new', function() {
    describe('basic test fo Survey.new modal', function() {
      it('should call modal', inject(function($stateParams, $state,
        $uibModal) {
        var config = $state.get('variable.new');
        spyOn($uibModal, 'open').and.callThrough();
        expect(config.url).toEqual('/new');
        config.onEnter[3]($stateParams, $state, $uibModal);
        expect(config.onEnter).toBeDefined();
        expect($uibModal.open).toHaveBeenCalled();
      }));
    });
    describe('basic test fo variable.detail modal promises', function() {
      it('should open modal', inject(function($stateParams, $state,
        $uibModal) {
        var modalOptions = {
          templateUrl: 'fakeUrl/variable-dialog.html.tmpl'
        };
        var fakeModal = {
          result: {
            then: function(confirmCallback, cancelCallback) {
              confirmCallback();
              cancelCallback();
            }
          }
        };
        spyOn($uibModal, 'open').and.returnValue(fakeModal);
        spyOn($state, 'go').and.callThrough();
        var config = $state.get('variable.new');
        config.onEnter[3]($stateParams, $state, $uibModal);
        expect($state.go).toHaveBeenCalled();
      }));
    });
  });
  describe('variable.edit', function() {
    describe('basic test fo variable.edit modal', function() {
      it('should call modal', inject(function($stateParams, $state,
        $uibModal) {
        var config = $state.get('variable.edit');
        spyOn($uibModal, 'open').and.callThrough();
        expect(config.url).toEqual('/{id}/edit');
        config.onEnter[3]($stateParams, $state, $uibModal);
        expect(config.onEnter).toBeDefined();
        expect($uibModal.open).toHaveBeenCalled();
      }));
    });
    describe('basic test fo survey.edit modal promises', function() {
      it('should open modal', inject(function($rootScope,
        $stateParams, $state, $uibModal) {
        var modalOptions = {
          templateUrl: 'fakeUrl/variable-edit.html'
        };
        var fakeModal = {
          result: {
            then: function(confirmCallback, cancelCallback) {
              confirmCallback();
              cancelCallback();
            }
          }
        };
        spyOn($uibModal, 'open').and.returnValue(fakeModal);
        spyOn($state, 'go').and.callThrough();
        var config = $state.get('variable.edit');
        try {
          config.onEnter[3]($stateParams, $state, $uibModal);
        } catch (e) {

        }
        expect($state.go).toHaveBeenCalled();
      }));
    });
  });
});
