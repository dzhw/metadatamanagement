/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */

'use strict';

describe('dataAcquisitionProject api', function() {
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
  describe('basic test dataAcquisitionProject api', function() {
    it('should call $translate and $translatePartialLoader', function() {
      var config = $state.get('dataAcquisitionProject');
      expect(config.url).toEqual('/data-acquisition-projects');
      expect(config.resolve.translatePartialLoader).toBeDefined();
      $state.go('dataAcquisitionProject');
      expect($translate.refresh).toHaveBeenCalled();
      expect($translatePartialLoader.addPart).toHaveBeenCalled();
    });
  });
  describe('basic test fo dataAcquisitionProject.detail', function() {
    it('should call $translate, $translatePartialLoader and' +
    'DataAcquisitionProject',
      inject(function(_DataAcquisitionProjectResource_) {
        var config = $state.get('dataAcquisitionProject.detail');
        var DataAcquisitionProject = _DataAcquisitionProjectResource_;
        spyOn(DataAcquisitionProject, 'get').and.callThrough();
        expect(config.url).toEqual('/data-acquisition-projects/{id}');
        expect(config.resolve.translatePartialLoader).toBeDefined();
        expect(config.resolve.entity).toBeDefined();
        $state.go('dataAcquisitionProject.detail');
        expect($translate.refresh).toHaveBeenCalled();
        expect($translatePartialLoader.addPart).toHaveBeenCalled();
        expect(DataAcquisitionProject.get).toHaveBeenCalled();
      }));
  });
  describe('dataAcquisitionProject.new', function() {
    describe('basic test fo dataAcquisitionProject.new modal', function() {
      it('should call modal', inject(function($stateParams, $state,
        $uibModal) {
        var config = $state.get('dataAcquisitionProject.new');
        spyOn($uibModal, 'open').and.callThrough();
        expect(config.url).toEqual('/new');
        config.onEnter[3]($stateParams, $state, $uibModal);
        expect(config.onEnter).toBeDefined();
        expect($uibModal.open).toHaveBeenCalled();
      }));
    });
    describe('basic test fo dataAcquisitionProject.detail modal promises',
    function() {
      it('should open modal', inject(function($stateParams, $state,
        $uibModal) {
        var modalOptions = {
          templateUrl: 'fakeUrl/dataAcquisitionProject-dialog.html.tmpl'
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
        var config = $state.get('dataAcquisitionProject.new');
        config.onEnter[3]($stateParams, $state, $uibModal);
        expect($state.go).toHaveBeenCalled();
      }));
    });
  });
  describe('dataAcquisitionProject.edit', function() {
    describe('basic test fo dataAcquisitionProject.edit modal', function() {
      it('should call modal', inject(function($stateParams, $state,
        $uibModal) {
        var config = $state.get('dataAcquisitionProject.edit');
        spyOn($uibModal, 'open').and.callThrough();
        expect(config.url).toEqual('/{id}/edit');
        config.onEnter[3]($stateParams, $state, $uibModal);
        expect(config.onEnter).toBeDefined();
        expect($uibModal.open).toHaveBeenCalled();
      }));
    });
    describe('basic test fo dataAcquisitionProject.edit modal promises',
    function() {
      it('should open modal', inject(function($rootScope,
        $stateParams, $state, $uibModal) {
        var modalOptions = {
          templateUrl: 'fakeUrl/dataAcquisitionProject-edit.html'
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
        var config = $state.get('dataAcquisitionProject.edit');
        try {
          config.onEnter[3]($stateParams, $state, $uibModal);
        } catch (e) {

        }
        expect($state.go).toHaveBeenCalled();
      }));
    });
  });
  describe('dataAcquisitionProject.delete', function() {
    describe('basic test fo dataAcquisitionProject.delete modal', function() {
      it('should call modal', inject(function($stateParams, $state,
        $uibModal) {
        var config = $state.get('dataAcquisitionProject.delete');
        spyOn($uibModal, 'open').and.callThrough();
        expect(config.url).toEqual('/{id}/delete');
        config.onEnter[3]($stateParams, $state, $uibModal);
        expect(config.onEnter).toBeDefined();
        expect($uibModal.open).toHaveBeenCalled();
      }));
    });
    describe('basic test fo dataAcquisitionProject.delete modal promises',
    function() {
      it('should open modal', inject(function($rootScope,
        $stateParams, $state, $uibModal) {
        var modalOptions = {
          templateUrl: 'fakeUrl/dataAcquisitionProject-delete.html'
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
        var config = $state.get('dataAcquisitionProject.delete');
        try {
          config.onEnter[3]($stateParams, $state, $uibModal);
        } catch (e) {

        }
        expect($state.go).toHaveBeenCalled();
      }));
    });
  });
});
