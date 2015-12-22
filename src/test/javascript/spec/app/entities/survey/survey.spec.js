'use strict';

describe('survey api', function () {
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
       describe('basic test survey api', function() {
         it('should call $translate and $translatePartialLoader', function() {
           var config = $state.get('survey');
           expect(config.url).toEqual('/surveys');
           expect(config.resolve.translatePartialLoader).toBeDefined();
           config.resolve.translatePartialLoader[2]($translate, $translatePartialLoader);
           expect($translate.refresh).toHaveBeenCalled();
           expect($translatePartialLoader.addPart).toHaveBeenCalled();
         });
       });
       describe('basic test fo survey.detail', function(){
         it('should call $translate, $translatePartialLoader and survey',
         inject(function(_Survey_, $stateParams) {
           var config = $state.get('survey.detail');
           var Survey = _Survey_;
           spyOn(Survey,'get').and.callThrough();
           expect(config.url).toEqual('/survey/{id}');
           expect(config.resolve.translatePartialLoader).toBeDefined();
           expect(config.resolve.entity).toBeDefined();
           config.resolve.translatePartialLoader[2]($translate, $translatePartialLoader);
           config.resolve.entity[2]($stateParams, Survey);
           expect($translate.refresh).toHaveBeenCalled();
           expect($translatePartialLoader.addPart).toHaveBeenCalled();
           expect(Survey.get).toHaveBeenCalled();
         }));
       });
       describe('Survey.new', function(){
         describe('basic test fo Survey.new modal', function(){
           it('should call modal', inject(function($stateParams, $state, $uibModal) {
             var config = $state.get('survey.new');
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
              templateUrl: 'fakeUrl/survey-dialog.html'
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
            var config = $state.get('survey.new');
            config.onEnter[3]($stateParams, $state, $uibModal);
            expect($state.go).toHaveBeenCalled();
          }));
        });
      });
      describe('survey.edit', function(){
        describe('basic test fo survey.edit modal', function(){
          it('should call modal', inject(function($stateParams, $state, $uibModal) {
            var config = $state.get('survey.edit');
            spyOn($uibModal,'open').and.callThrough();
            expect(config.url).toEqual('/{id}/edit');
            config.onEnter[3]($stateParams, $state, $uibModal);
            expect(config.onEnter).toBeDefined();
            expect($uibModal.open).toHaveBeenCalled();
          }));
        });
        describe('basic test fo survey.edit modal promises', function(){
         it('should open modal', inject(function($rootScope, $stateParams, $state, $uibModal) {
           var modalOptions = {
             templateUrl: 'fakeUrl/survey-edit.html'
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
           var config = $state.get('survey.edit');
           try {
             config.onEnter[3]($stateParams, $state, $uibModal);
           } catch (e) {

           }
           expect($state.go).toHaveBeenCalled();
         }));
       });
     });
     describe('survey.delete', function(){
       describe('basic test fo survey.delete modal', function(){
         it('should call modal', inject(function($stateParams, $state, $uibModal) {
           var config = $state.get('survey.delete');
           spyOn($uibModal,'open').and.callThrough();
           expect(config.url).toEqual('/{id}/delete');
           config.onEnter[3]($stateParams, $state, $uibModal);
           expect(config.onEnter).toBeDefined();
           expect($uibModal.open).toHaveBeenCalled();
         }));
       });
       describe('basic test fo survey.delete modal promises', function(){
        it('should open modal', inject(function($rootScope, $stateParams, $state, $uibModal) {
          var modalOptions = {
            templateUrl: 'fakeUrl/survey-delete.html'
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
