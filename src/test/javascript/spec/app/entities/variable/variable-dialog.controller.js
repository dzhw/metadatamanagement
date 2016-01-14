'use strict';

describe('Controllers Tests ', function () {
    var $scope, $rootScope, $uibModalInstance, MockEntity, FdzProject, Survey, Variable, createController;
        beforeEach(mockApiAccountCall);
        beforeEach(mockI18nCalls);
    describe('VariableDialogController', function() {
        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $injector.get('$rootScope').$new();
            MockEntity = jasmine.createSpy('MockEntity');
            $uibModalInstance = {
              dismiss: jasmine.createSpy('$uibModalInstance.cancel'),
              close: jasmine.createSpy('$uibModalInstance.close'),
              result: {
                then: jasmine.createSpy('$uibModalInstance.result.then')
              }
            };
            FdzProject = {
              query: function(){
                return {
                   then: function(callback){
                     return callback();
                   }
                };
              }
            };
            Survey = {
              query: function(){
                return {
                   then: function(callback){
                     return callback();
                   }
                };
              }
            };
            Variable = {
              get: function(){
                return {
                   then: function(callback){
                     return callback();
                   }
                };
              },
              create: function(){
                return {
                   then: function(callback){
                     return callback();
                   }
                };
              }
            };
          var locals = {
            '$scope': $scope,
            '$uibModalInstance': $uibModalInstance,
            'entity': MockEntity,
            'isCreateMode' : false,
            'FdzProject': FdzProject,
            'Survey': Survey,
            'Variable': Variable
          };
          createController = function() {
            $injector.get('$controller')('VariableDialogController',locals);
          };
            spyOn(FdzProject, 'query').and.callThrough();
            spyOn(Survey, 'query').and.callThrough();
            spyOn(Variable, 'get').and.callThrough();
            //spyOn(Variable, 'create').and.callThrough();
        }));
        beforeEach(function(){
          createController();
        });
        it('should call $uibModalInstance.dismiss', function() {
          $scope.clear();
          expect($uibModalInstance.dismiss).toHaveBeenCalled();
        });
        it('should set allFdzProjects', function() {
           expect($scope.allFdzProjects).toBeDefined();
        });
        it('should call Variable.get', function() {
          $scope.load();
          expect(Variable.get).toHaveBeenCalled();
        });
        it('should call Survey.query', function() {
        $scope.changeSurvey();
          expect(Survey.query).toHaveBeenCalled();
        });
        it('should call Survey.query', function() {
        //  $scope.allSurveysByFdzProjectName = {};
          var val = $scope.isSurveyEmpty();
          expect(val).toBe(true);
        });
        xit('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'metadatamanagementApp:variableUpdate';


          $scope.save();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

          /*  $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();*/
        });
      });
    });
