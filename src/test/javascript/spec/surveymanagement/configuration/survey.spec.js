/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */
/* global jasmine */

'use strict';

describe('survey api', function() {
  var $translate;
  var $translatePartialLoader;
  var $state;
  var Survey;
  beforeEach(inject(function(_$translate_, _$translatePartialLoader_,
    _$httpBackend_, _$state_) {
    $translate = _$translate_;
    $translatePartialLoader = _$translatePartialLoader_;
    $state = _$state_;
    Survey = jasmine.createSpy('Survey');
    var globalJson = new RegExp('i18n\/.*\/global.json');
    var mainJson = new RegExp('i18n\/.*\/main.json');
    _$httpBackend_.whenGET(globalJson).respond({});
    _$httpBackend_.whenGET(mainJson).respond({});
    _$httpBackend_.expectGET(/api\/account\?cacheBuster=\d+/).respond(
      200, '');
    spyOn($translate, 'refresh').and.callThrough();
    spyOn($translatePartialLoader, 'addPart').and.callThrough();
  }));
  describe('basic test survey api', function() {
    it('should call $translate and $translatePartialLoader', function() {
      var config = $state.get('survey');
      expect(config.url).toEqual('/surveys');
      expect(config.resolve.translatePartialLoader).toBeDefined();
      $state.go('survey');
      expect($translate.refresh).toHaveBeenCalled();
      expect($translatePartialLoader.addPart).toHaveBeenCalled();
    });
  });
  describe('basic test fo survey.detail', function() {
    it('should call $translate, $translatePartialLoader and survey',
      inject(function(_SurveyResource_) {
        var config = $state.get('survey.detail');
        var SurveyResource = _SurveyResource_;
        spyOn(SurveyResource, 'get').and.callThrough();
        expect(config.url).toEqual('/surveys/{id}');
        expect(config.resolve.translatePartialLoader).toBeDefined();
        expect(config.resolve.entity).toBeDefined();
        $state.go('survey.detail');
        expect($translate.refresh).toHaveBeenCalled();
        expect($translatePartialLoader.addPart).toHaveBeenCalled();
        expect(SurveyResource.get).toHaveBeenCalled();
      }));
  });
  describe('Survey.new', function() {
    describe('basic test fo Survey.new modal', function() {
      it('should call modal', inject(function($stateParams, $state,
        $uibModal, _SurveyResource_) {
        var SurveyResource = _SurveyResource_;
        var config = $state.get('survey.new');
        spyOn($uibModal, 'open').and.callThrough();
        expect(config.url).toEqual('/new');
        config.onEnter[4]($stateParams, $state, $uibModal,
          SurveyResource);
        expect(config.onEnter).toBeDefined();
        expect($uibModal.open).toHaveBeenCalled();
      }));
    });
    describe(
      'basic test fo data acquisition Project.detail modal promises',
      function() {
        it('should open modal', inject(function($stateParams, $state,
          $uibModal, _SurveyResource_) {
          var modalOptions = {
            templateUrl: 'fakeUrl/survey-dialog.html.tmpl'
          };
          var fakeModal = {
            result: {
              then: function(confirmCallback, cancelCallback) {
                confirmCallback();
                cancelCallback();
              }
            }
          };
          var SurveyResource = _SurveyResource_;
          spyOn($uibModal, 'open').and.returnValue(fakeModal);
          spyOn($state, 'go').and.callThrough();
          var config = $state.get('survey.new');
          config.onEnter[4]($stateParams, $state, $uibModal,
            SurveyResource);
          expect($state.go).toHaveBeenCalled();
        }));
      });
  });
  describe('survey.edit', function() {
    describe('basic test fo survey.edit modal', function() {
      it('should call modal', inject(function($stateParams, $state,
        $uibModal) {
        var config = $state.get('survey.edit');
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
          templateUrl: 'fakeUrl/survey-edit.html'
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
        var config = $state.get('survey.edit');
        try {
          config.onEnter[3]($stateParams, $state, $uibModal);
        } catch (e) {

        }
        expect($state.go).toHaveBeenCalled();
      }));
    });
  });
  describe('survey.delete', function() {
    describe('basic test fo survey.delete modal', function() {
      it('should call modal', inject(function($stateParams, $state,
        $uibModal) {
        var config = $state.get('survey.delete');
        spyOn($uibModal, 'open').and.callThrough();
        expect(config.url).toEqual('/{id}/delete');
        config.onEnter[3]($stateParams, $state, $uibModal);
        expect(config.onEnter).toBeDefined();
        expect($uibModal.open).toHaveBeenCalled();
      }));
    });
    describe('basic test fo survey.delete modal promises', function() {
      it('should open modal', inject(function($rootScope,
        $stateParams, $state, $uibModal) {
        var modalOptions = {
          templateUrl: 'fakeUrl/survey-delete.html'
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
        var config = $state.get('survey.delete');
        try {
          config.onEnter[3]($stateParams, $state, $uibModal);
        } catch (e) {

        }
        expect($state.go).toHaveBeenCalled();
      }));
    });
  });
});
