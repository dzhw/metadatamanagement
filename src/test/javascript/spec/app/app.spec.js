'use strict';

describe('Specification for app ', function () {
        var event, toState, toParams, fromState, fromParams,
        $scope, $httpBackend, $rootScope, $location, Language,
        $state, $stateParams, $window, $translate;
        describe('metadatamanagementApp run function',function(){
          beforeEach(function(){inject(function (_$rootScope_, _$location_,
            _$httpBackend_,_Language_, _$state_, _$stateParams_) {
            $rootScope = _$rootScope_;
            $scope = _$rootScope_.$new();
            $location = _$location_;
            $httpBackend = _$httpBackend_;
            $state = _$state_;
            Language = _Language_;
            $state = _$state_;
            $stateParams = _$stateParams_;
          });
          var globalJson = new RegExp('i18n\/.*\/global.json')
          var mainJson = new RegExp('i18n\/.*\/main.json');
          $httpBackend.whenGET(globalJson).respond({});
          $httpBackend.whenGET(mainJson).respond({});
          $httpBackend.expectGET(/api\/account\?cacheBuster=\d+/).respond(200, '');
        });
        it('should set Language to de ', function () {
          expect(Language.getCurrentInstantly()).toBe('de');
        });
        it('should set Language to en ', function () {
          $location.path('/en/');
          $rootScope.$apply();
          expect(Language.getCurrentInstantly()).toBe('en');
        });
        describe('run functions',function(){
          describe('on stateChangeStart', function(){
            it('should call stateChangeStart function', function () {
              spyOn($rootScope,'$broadcast');
              $rootScope.$broadcast('$stateChangeStart', event, toState, toParams);
              expect($scope.$broadcast).toHaveBeenCalledWith('$stateChangeStart', event, toState, toParams);
            });
          });
          describe('on stateChangeSuccess', function(){
            beforeEach(function(){
              fromState   = { name:'disclosure', parent: 'site',url: '/disclosure' };
              fromParams  = { name:'testParams' };
              toState = { name: 'home',parent: 'site',url: '/'};
              toParams = { name:'home' };
              $rootScope.toState = toState;
              $rootScope.previousStateName = 'previousStateName';
              $rootScope.toState = toState;
              $rootScope.fromState = fromState;
              $rootScope.toParams = toParams;
              $rootScope.fromParams = fromParams;
              event = { currentScope: $rootScope, defaultPrevented: true,
                name: 'disclosure', data: { authorities: [], pageTitle:'global.title'} };
              spyOn($rootScope,'$broadcast').and.callThrough();
              $rootScope.$broadcast('$stateChangeSuccess', event, toState, toParams,fromState, fromParams);
              });
              it('should call stateChangeSuccess function', function () {
                expect($scope.$broadcast).toHaveBeenCalledWith('$stateChangeSuccess', event, toState, toParams,fromState, fromParams);
              });
              it('should set previousStateParams to testState ', function () {
                expect($rootScope.previousStateParams.name).toBe('disclosure');
              });
            });
          });
        });
        describe('translateProvider ',function(){
          beforeEach(module(function($translateProvider, $provide) {
            $translateProvider.useLoader('customLoader');
            $provide.service('customLoader', function($q) {
              return function() {
                var deferred = $q.defer();
                deferred.resolve({
                  "global.title": "metadatamanagement"
                });
                return deferred.promise;
              };
            });
          }));
            it('should translates page title', inject(function($rootScope, $httpBackend, $window) {
              var globalJson = new RegExp('i18n\/.*\/global.json')
              var mainJson = new RegExp('i18n\/.*\/main.json');
              $httpBackend.whenGET(globalJson).respond({});
              $httpBackend.whenGET(mainJson).respond({});
              $httpBackend.expectGET(/api\/account\?cacheBuster=\d+/).respond(200, '');
                $rootScope.$broadcast('$stateChangeSuccess', event, toState, toParams);
                $rootScope.$apply();
                expect(  $window.document.title).toEqual('metadatamanagement');
              }));
            });
            describe('back function',function(){
              it('should call back function', function () {
                spyOn($scope,'back').and.callThrough();
                spyOn($state,'go').and.callThrough();
                $scope.previousStateName = 'activate';
                $scope.back();
                expect($scope.back).toHaveBeenCalled();
                expect($state.go).toHaveBeenCalled();
              });
            })

});
