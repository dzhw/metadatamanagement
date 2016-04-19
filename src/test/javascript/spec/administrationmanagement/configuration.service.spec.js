'use strict';

describe('Factory Tests ', function () {
  var ConfigurationService, $httpMock, $q, $scope;
    describe('ConfigurationService', function() {
      beforeEach(module(function ($provide) {
          $httpMock = jasmine.createSpyObj('$http',['get']);
          $provide.value('$http', $httpMock);
    }));
        beforeEach(inject(function($injector) {
            ConfigurationService = $injector.get('ConfigurationService');
            $q = $injector.get('$q');
            $scope = $injector.get('$rootScope').$new();
        }));
        it('ConfigurationService.get should be defined', function() {
          expect(ConfigurationService.get).toBeDefined();
        });
        it(' should make a GET request', function() {
          var data = {
            'data': 'admin'
          };
          var defer = $q.defer();
          defer.resolve(data);
          $httpMock.get.and.returnValue(defer.promise);
          ConfigurationService.get();
          $scope.$digest();
          expect($httpMock.get).toHaveBeenCalled();
        });
      });
    });
