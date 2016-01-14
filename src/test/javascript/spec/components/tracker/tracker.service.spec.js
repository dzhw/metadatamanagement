'use strict';

describe('Factory Tests ', function () {
  var Tracker, $httpMock, $q, $scope;
    describe('Tracker', function() {
      beforeEach(module(function ($provide) {
          $httpMock = jasmine.createSpyObj('$http',['post']);
          $provide.value('$http', $httpMock);
    }));
        beforeEach(inject(function($injector) {
            Tracker = $injector.get('Tracker');
            $q = $injector.get('$q');
            $scope = $injector.get('$rootScope').$new();
        }));
        it('Tracker.connect should be defined', function() {
          expect(Tracker.connect).toBeDefined();
        });
        xit('sdsfdfsracker.connect should be defined', function() {
          Tracker.connect();
        });
        xit('sdsfdfsracker.connect should be defined', function() {
          Tracker.subscribe();
        });
        xit('sdsfdfsracker.connect should be defined', function() {
          Tracker.unsubscribe();
        });
        it('sdsfdfsracker.connect should be defined', function() {
          Tracker.receive();
        });
        it('sdsfdfsracker.connect should be defined', function() {
          Tracker.sendActivity();
        });
        it('sdsfdfsracker.connect should be defined', function() {
          Tracker.disconnect();
        });
      });
    });
