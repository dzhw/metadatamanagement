'use strict';

describe(
    'Controllers Tests ',
    function() {
      var $scope, $uibModalInstance, $q, metrics, actualOptions, MonitoringService, newValue, createController;
      beforeEach(mockApiAccountCall);
      beforeEach(mockI18nCalls);
      describe('MetricsController', function() {
        beforeEach(inject(function($injector) {
          $scope = $injector.get('$rootScope').$new();
          $q = $injector.get('$q');
          $uibModalInstance = {
            open : jasmine.createSpy('$uibModalInstance.open'),
            result : {
              then : jasmine.createSpy('$uibModalInstance.result.then')
            }
          };
          MonitoringService = {
            getMetrics : function() {
              var deferred = $q.defer();
              deferred.resolve();
              deferred.reject({});
              return deferred.promise;
            },
            threadDump : function() {
              var deferred = $q.defer();
              deferred.resolve();
              deferred.reject({});
              return deferred.promise;
            }
          };
          var locals = {
            '$scope' : $scope,
            'MonitoringService' : MonitoringService,
            '$uibModal' : $uibModalInstance,
          };
          createController = function() {
            $injector.get('$controller')('MetricsController', locals);
          };
          spyOn(MonitoringService, 'getMetrics').and.callThrough();
          spyOn(MonitoringService, 'threadDump').and.callThrough();
        }));
        beforeEach(function() {
          createController();
        });
        it('should set $scope.updatingMetrics when resolve to false',
            function() {
              MonitoringService.getMetrics.and.returnValue($q.resolve());
              try {
                $scope.$apply(createController);
              } catch (e) {
              }
              expect($scope.updatingMetrics).toBe(false);
            });
        it('should set $scope.updatingMetrics when reject to false',
            function() {
              MonitoringService.getMetrics.and.returnValue($q.reject({}));
              try {
                $scope.$apply(createController);
              } catch (e) {

              }
              expect($scope.updatingMetrics).toBe(false);
            });
        it('should set $scope.updatingMetrics to false', function() {
          try {
            $scope.$apply(createController);
          } catch (e) {

          }
          $scope.refreshThreadDumpData();
          expect(MonitoringService.threadDump).toHaveBeenCalled();
        });
        it('should set $scope.updatingMetrics to false', function() {
          MonitoringService.threadDump.and.returnValue($q.resolve());
          $scope.refreshThreadDumpData();
          try {
            $scope.$apply(createController);
          } catch (e) {

          }
          expect($uibModalInstance.open).toHaveBeenCalled();
        });
      });
    });
