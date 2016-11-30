/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global spyOn */
/* global xit */

'use strict';

describe('Specification for app ', function() {
  var event;
  var toState;
  var toParams;
  var fromState;
  var fromParams;
  var $scope;
  var $httpBackend;
  var $rootScope;
  var $location;
  var LanguageService;
  var $state;
  var $stateParams;

  describe('metadatamanagementApp run function', function() {
    beforeEach(function() {
      inject(function(_$rootScope_, _$location_,
        _$httpBackend_, _LanguageService_, _$state_,
        _$stateParams_) {
        $rootScope = _$rootScope_;
        $scope = _$rootScope_.$new();
        $location = _$location_;
        $httpBackend = _$httpBackend_;
        $state = _$state_;
        LanguageService = _LanguageService_;
        $state = _$state_;
        $stateParams = _$stateParams_;
      });
      var globalJson = new RegExp('i18n\/.*\/global.json');
      var searchJson = new RegExp(
        'i18n\/.*\/search.management.json');
      var dataSetJson = new RegExp(
        'i18n\/.*\/dataSet.management.json');
      var variableJson = new RegExp(
        'i18n\/.*\/variable.management.json');
      var questionJson = new RegExp(
        'i18n\/.*\/question.management.json');
      var dataAcquisitionProjectJson =
        new RegExp(
          'i18n\/.*\/dataAcquisitionProject.management.json');
      $httpBackend.whenGET(globalJson).respond({});
      $httpBackend.whenGET(searchJson).respond({});
      $httpBackend.whenGET(dataSetJson).respond({});
      $httpBackend.whenGET(dataAcquisitionProjectJson).respond({});
      $httpBackend.whenGET(variableJson).respond({});
      $httpBackend.whenGET(questionJson).respond({});
      $httpBackend.expectGET(/scripts\/common\/navbar\/views\/navbar.html.tmpl/)
        .respond(200, '');
      $httpBackend.expectGET(/scripts\/common\/toolbar\/views\/toolbar.html.tmpl/)
        .respond(200, '');
    });
    it('should set LanguageService to de ', function() {
      expect(LanguageService.getCurrentInstantly()).toBe('de');
    });
    it('should set LanguageService to en ', function() {
      $location.path('/en/');
      $rootScope.$apply();
      expect(LanguageService.getCurrentInstantly()).toBe('en');
    });
    describe('run functions', function() {
      describe('on stateChangeStart', function() {
        it('should call stateChangeStart function', function() {
          spyOn($rootScope, '$broadcast');
          $rootScope.$broadcast('$stateChangeStart', event,
            toState, toParams);
          expect($scope.$broadcast).toHaveBeenCalledWith(
            '$stateChangeStart', event, toState, toParams
          );
        });
      });
      describe('on stateChangeSuccess', function() {
        beforeEach(function() {
          fromState = {
            name: 'disclosure',
            parent: 'site',
            url: '/disclosure'
          };
          fromParams = {
            name: 'testParams'
          };
          toState = {
            name: 'search',
            parent: 'site',
            url: '/'
          };
          toParams = {
            name: 'search'
          };
          $rootScope.toState = toState;
          $rootScope.previousStateName =
            'previousStateName';
          $rootScope.toState = toState;
          $rootScope.fromState = fromState;
          $rootScope.toParams = toParams;
          $rootScope.fromParams = fromParams;
          event = {
            currentScope: $rootScope,
            defaultPrevented: true,
            name: 'disclosure',
            data: {
              authorities: [],
              pageTitle: 'global.title'
            }
          };
          spyOn($rootScope, '$broadcast').and.callThrough();
          $rootScope.$broadcast('$stateChangeSuccess',
            event,
            toState, toParams, fromState, fromParams);
        });
        it('should call stateChangeSuccess function', function() {
          expect($scope.$broadcast).toHaveBeenCalledWith(
            '$stateChangeSuccess', event, toState,
            toParams, fromState, fromParams);
        });
        it('should set previousStateParams to testState ',
          function() {
            expect($rootScope.previousStateParams.name).toBe(
              'disclosure');
          });
      });
    });
  });
  describe('back function', function() {
    it('should call back function', function() {
      spyOn($scope, 'back').and.callThrough();
      spyOn($state, 'go').and.callThrough();
      $scope.previousStateName = 'activate';
      $scope.back();
      expect($scope.back).toHaveBeenCalled();
      expect($state.go).toHaveBeenCalled();
    });
    it('should call back function', function() {
      fromState = {};
      fromParams = {};
      toState = {};
      toParams = {};
      $rootScope.toState = toState;
      $rootScope.previousStateName = 'previousStateName';
      $rootScope.toState = toState;
      $rootScope.fromState = fromState;
      $rootScope.toParams = toParams;
      $rootScope.fromParams = fromParams;
      spyOn($scope, 'back').and.callThrough();
      spyOn($state, 'go').and.callThrough();
      $scope.previousStateName = 'activate';
      $scope.back();
      expect($scope.back).toHaveBeenCalled();
      expect($state.go).toHaveBeenCalledWith('search', {
        lang: 'de'
      });
    });
  });
  describe('metadatamanagementApp configuration ', function() {
    var $urlMatcherFactory;
    beforeEach(function() {
      module(function(_$urlMatcherFactoryProvider_) {
        $urlMatcherFactory = _$urlMatcherFactoryProvider_;
        spyOn($urlMatcherFactory, 'type').and.callThrough();
      });
      inject();
    });
    xit('should call back function', function() {
      expect($urlMatcherFactory.type('boolean')
        .decode('true')).toBe(true);
      expect($urlMatcherFactory.type('boolean')
        .decode('false')).toBe(false);
      expect($urlMatcherFactory.type('boolean')
        .decode('test')).toBe(false);
      expect($urlMatcherFactory.type('boolean')
        .decode(true)).toBe(true);
      expect($urlMatcherFactory.type('boolean')
        .decode(false)).toBe(false);
      expect($urlMatcherFactory.type('boolean')
        .encode('test')).toBe(1);
      expect($urlMatcherFactory.type('boolean')
        .encode('0')).toBe(1);
      expect($urlMatcherFactory.type('boolean')
        .encode('1')).toBe(1);
      expect($urlMatcherFactory.type('boolean')
        .encode()).toBe(0);
      expect($urlMatcherFactory.type('boolean')
        .equals(1, 2)).toBe(false);
      expect($urlMatcherFactory.type('boolean').is(1)).toBe(true);
    });
  });
});
