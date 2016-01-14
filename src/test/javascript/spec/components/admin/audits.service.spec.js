'use strict';

describe('Factory Tests ', function () {
  var AuditsService, $httpMock, $q, $scope, defer;
    describe('AuditsService', function() {
      beforeEach(module(function ($provide) {
          $httpMock = jasmine.createSpyObj('$http',['get']);
          $provide.value('$http', $httpMock);
    }));
        beforeEach(inject(function($injector) {
            AuditsService = $injector.get('AuditsService');
            $q = $injector.get('$q');
            $scope = $injector.get('$rootScope').$new();
            var data = {
              'data': 'admin'
            };
            defer = $q.defer();
            defer.resolve(data);
        }));
        it('AuditsService.findAll should be defined', function() {
          expect(AuditsService.findAll).toBeDefined();
        });
        it('AuditsService.findByDates should be defined', function() {
          expect(AuditsService.findByDates).toBeDefined();
        });
        it(' should make a get request by findAll', function() {
          $httpMock.get.and.returnValue(defer.promise);
          AuditsService.findAll();
          $scope.$digest();
          expect($httpMock.get).toHaveBeenCalled();
        });
        it(' should make a get request by findByDates', function() {
          $httpMock.get.and.returnValue(defer.promise);
          AuditsService.findByDates('12-12-2012','12-12-2012');
          $scope.$digest();
          expect($httpMock.get).toHaveBeenCalled();
        });
        it(' should not make a get request by findByDates', function() {
          $httpMock.get.and.returnValue(defer.promise);
          try {
            AuditsService.findByDates('test',6767);
          }catch(e){

          }
          $scope.$digest();
          expect($httpMock.get).not.toHaveBeenCalled();
        });
      });
    });
