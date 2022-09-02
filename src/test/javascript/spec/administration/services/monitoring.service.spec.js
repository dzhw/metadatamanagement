'use strict';

describe('Factory Tests ', function() {
  var MonitoringService, $httpBackend, $q, defer, $scope, data;
  describe('MonitoringService', function() {
    beforeEach(mockSso);
    beforeEach(inject(function($injector) {
      MonitoringService = $injector.get('MonitoringService');
      $q = $injector.get('$q');
      $scope = $injector.get('$rootScope').$new();
      $httpBackend = $injector.get('$httpBackend');
      data = {
        'data': 'admin'
      };
      defer = $q.defer();
      defer.resolve(data);
      $httpBackend.whenGET('/management/health').respond(200, defer.promise);
    }));
    it('MonitoringService.checkHealth should be defined', function() {
      expect(MonitoringService.checkHealth).toBeDefined();
    });
    it(' should make a GET request by checkHealth', function() {
      var result;
      MonitoringService.checkHealth().then(function(data) {
        result = data;
      });
      $httpBackend.flush();
      expect(result).toEqual(data);
    });
  });
});
