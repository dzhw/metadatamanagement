'use strict';

describe('Factory Tests ', function() {
      var MonitoringService, $httpMock, $q, defer, $scope;
      describe('MonitoringService', function() {
          beforeEach(module(function($provide) {
              $httpMock = jasmine.createSpyObj('$http', ['get']);
              $provide.value('$http', $httpMock);
            }));
          beforeEach(inject(function($injector) {
              MonitoringService = $injector.get('MonitoringService');
              $q = $injector.get('$q');
              $scope = $injector.get('$rootScope').$new();
              var data = {
                'data': 'admin'
              };
              defer = $q.defer();
              defer.resolve(data);
            }));
          it('MonitoringService.checkHealth should be defined', function() {
            expect(MonitoringService.checkHealth).toBeDefined();
          });
          it(' should make a GET request by checkHealth', function() {
            $httpMock.get.and.returnValue(defer.promise);
            MonitoringService.checkHealth();
            $scope.$digest();
            expect($httpMock.get).toHaveBeenCalled();
          });
        });
    });
