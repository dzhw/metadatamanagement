'use strict';

describe('Controllers Tests ', function () {
    var $scope, $uibModalInstance, value, createController;
        beforeEach(mockApiAccountCall);
        beforeEach(mockI18nCalls);
    describe('MetricsModalController', function() {
        beforeEach(inject(function($injector) {
            $scope = $injector.get('$rootScope').$new();
            $uibModalInstance = {
              dismiss: jasmine.createSpy('$uibModalInstance.cancel'),
              result: {
                then: jasmine.createSpy('$uibModalInstance.result.then')
              }
            };
          var locals = {
            '$scope': $scope,
            '$uibModalInstance': $uibModalInstance,
            'threadDump': [
              {
                threadState : 'RUNNABLE'
              },
              {
                  threadState : 'WAITING'
              },
              {
                  threadState : 'TIMED_WAITING'
              },
              {
                  threadState : 'BLOCKED'
              }
            ]

          };
          createController = function() {
            $injector.get('$controller')('MetricsModalController',locals);
          };
        }));
        beforeEach(function(){
          createController();
        });
        it('shold increase threaddump values', function() {
          expect($scope.threadDumpRunnable).toBe(1);
          expect($scope.threadDumpWaiting).toBe(1);
          expect($scope.threadDumpTimedWaiting).toBe(1);
          expect($scope.threadDumpBlocked).toBe(1);
        });
        it('should call $uibModalInstance.dismiss', function() {
          $scope.cancel();
          expect($uibModalInstance.dismiss).toHaveBeenCalled();
        });
        it('shold set label to success', function() {
          var label =   $scope.getLabelClass('RUNNABLE');
          expect(label).toBe('label-success');
        });
        it('shold set label to label-danger', function() {
          var label =   $scope.getLabelClass('BLOCKED');
          expect(label).toBe('label-danger');
        });
        it('shold set label to label-info', function() {
          var label =   $scope.getLabelClass('WAITING');
          expect(label).toBe('label-info');
        });
        it('shold set label to label-warning', function() {
          var label =   $scope.getLabelClass('TIMED_WAITING');
          expect(label).toBe('label-warning');
        });
      });
    });
