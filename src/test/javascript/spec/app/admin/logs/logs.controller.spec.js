'use strict';

describe('Controllers Tests ', function () {
    var $scope, $q, LogsService, createController;
        beforeEach(mockApiAccountCall);
        beforeEach(mockI18nCalls);
    describe('LogsController', function() {
        beforeEach(inject(function($injector) {
            $scope = $injector.get('$rootScope').$new();
            $q = $injector.get('$q');
            LogsService = $injector.get('LogsService');
          var locals = {
            '$scope': $scope,
            'LogsService': LogsService
          };
          createController = function() {
            $injector.get('$controller')('LogsController',locals);
          };
          spyOn(LogsService, 'findAll').and.callThrough();
          spyOn(LogsService, 'changeLevel').and.callThrough();
        }));
        beforeEach(function(){
          createController();
        });
        it('should call LogsService.findAll', function() {
        $scope.loggers;
        expect(LogsService.findAll).toHaveBeenCalled();
        });
        it('should call LogsService.changeLevel', function() {
         $scope.changeLevel('test','UP');
        expect(LogsService.changeLevel).toHaveBeenCalledWith({
          name: 'test',
          level: 'UP'
        }, jasmine.any(Function));
        });
      });
    });
