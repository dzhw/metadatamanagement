'use strict';

xdescribe('fdzProject api', function () {
       var $translate, $translatePartialLoader, $state;
       beforeEach(inject(function(_$translate_, _$translatePartialLoader_, _$httpBackend_, _$state_){
         $translate = _$translate_;
         $translatePartialLoader = _$translatePartialLoader_;
         $state = _$state_;
         var globalJson = new RegExp('i18n\/.*\/global.json')
         var mainJson = new RegExp('i18n\/.*\/main.json');
         _$httpBackend_.whenGET(globalJson).respond({});
         _$httpBackend_.whenGET(mainJson).respond({});
         _$httpBackend_.expectGET(/api\/account\?cacheBuster=\d+/).respond(200, '');
         spyOn($translate,'refresh').and.callThrough();
         spyOn($translatePartialLoader,'addPart').and.callThrough();
       }));
       describe('basic test fdzProject api', function() {
         it('should call $translate and $translatePartialLoader', function() {
           var config = $state.get('fdzProject');
           expect(config.url).toEqual('/fdzProjects');
           expect(config.resolve.translatePartialLoader).toBeDefined();
           config.resolve.translatePartialLoader[2]($translate, $translatePartialLoader);
           expect($translate.refresh).toHaveBeenCalled();
           expect($translatePartialLoader.addPart).toHaveBeenCalled();
         });
       });
       describe('basic test fo fdzProject.detail', function(){
         it('should call $translate, $translatePartialLoader and FdzProject',
         inject(function(_FdzProject_, $stateParams) {
           var config = $state.get('fdzProject.detail');
           var FdzProject = _FdzProject_;
           spyOn(FdzProject,'get').and.callThrough();
           expect(config.url).toEqual('/fdzProject/{name}');
           expect(config.resolve.translatePartialLoader).toBeDefined();
           expect(config.resolve.entity).toBeDefined();
           config.resolve.translatePartialLoader[2]($translate, $translatePartialLoader);
           config.resolve.entity[2]($stateParams, FdzProject);
           expect($translate.refresh).toHaveBeenCalled();
           expect($translatePartialLoader.addPart).toHaveBeenCalled();
           expect(FdzProject.get).toHaveBeenCalled();
         }));
       });
       describe('fdzProject.new', function(){
         describe('basic test fo fdzProject.new modal', function(){
           it('should call modal', inject(function($stateParams, $state, $uibModal) {
             var config = $state.get('fdzProject.new');
             spyOn($uibModal,'open').and.callThrough();
             expect(config.url).toEqual('/new');
             config.onEnter[3]($stateParams, $state, $uibModal);
             expect(config.onEnter).toBeDefined();
             expect($uibModal.open).toHaveBeenCalled();
           }));
         });
         describe('basic test fo fdzProject.detail modal promises', function(){
          it('should open modal', inject(function($stateParams, $state, $uibModal) {
            var modalOptions = {
              templateUrl: 'fakeUrl/fdzProject-dialog.html.tmpl'
            };
            var fakeModal = {
              result: {
                then: function (confirmCallback, cancelCallback) {
                  confirmCallback();
                  cancelCallback();
                }
              }
            };
            spyOn($uibModal, 'open').and.returnValue(fakeModal);
            spyOn($state,'go').and.callThrough();
            var config = $state.get('fdzProject.new');
            config.onEnter[3]($stateParams, $state, $uibModal);
            expect($state.go).toHaveBeenCalled();
          }));
        });
      });
      describe('fdzProject.edit', function(){
        describe('basic test fo fdzProject.edit modal', function(){
          it('should call modal', inject(function($stateParams, $state, $uibModal) {
            var config = $state.get('fdzProject.edit');
            spyOn($uibModal,'open').and.callThrough();
            expect(config.url).toEqual('/{name}/edit');
            config.onEnter[3]($stateParams, $state, $uibModal);
            expect(config.onEnter).toBeDefined();
            expect($uibModal.open).toHaveBeenCalled();
          }));
        });
        describe('basic test fo fdzProject.edit modal promises', function(){
         it('should open modal', inject(function($rootScope, $stateParams, $state, $uibModal) {
           var modalOptions = {
             templateUrl: 'fakeUrl/fdzProject-edit.html'
           };
           var fakeModal = {
             result: {
               then: function (confirmCallback, cancelCallback) {
                 confirmCallback();
                 cancelCallback();
               }
             }
           };
           spyOn($uibModal, 'open').and.returnValue(fakeModal);
           spyOn($state,'go').and.callThrough();
           var config = $state.get('fdzProject.edit');
           try {
             config.onEnter[3]($stateParams, $state, $uibModal);
           } catch (e) {

           }
           expect($state.go).toHaveBeenCalled();
         }));
       });
     });
     describe('fdzProject.delete', function(){
       describe('basic test fo fdzProject.delete modal', function(){
         it('should call modal', inject(function($stateParams, $state, $uibModal) {
           var config = $state.get('fdzProject.delete');
           spyOn($uibModal,'open').and.callThrough();
           expect(config.url).toEqual('/{name}/delete');
           config.onEnter[3]($stateParams, $state, $uibModal);
           expect(config.onEnter).toBeDefined();
           expect($uibModal.open).toHaveBeenCalled();
         }));
       });
       describe('basic test fo fdzProject.delete modal promises', function(){
        it('should open modal', inject(function($rootScope, $stateParams, $state, $uibModal) {
          var modalOptions = {
            templateUrl: 'fakeUrl/fdzProject-delete.html'
          };
          var fakeModal = {
            result: {
              then: function (confirmCallback, cancelCallback) {
                confirmCallback();
                cancelCallback();
              }
            }
          };
          spyOn($uibModal, 'open').and.returnValue(fakeModal);
          spyOn($state,'go').and.callThrough();
          var config = $state.get('fdzProject.delete');
          try {
            config.onEnter[3]($stateParams, $state, $uibModal);
          } catch (e) {

          }
          expect($state.go).toHaveBeenCalled();
        }));
      });
    });



});
