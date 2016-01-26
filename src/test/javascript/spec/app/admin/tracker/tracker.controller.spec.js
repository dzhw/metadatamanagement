'use strict';

describe('Controllers Tests ', function () {
    var $scope, $q, AuthServerProvider, Tracker, createController;
        beforeEach(mockApiAccountCall);
        beforeEach(mockI18nCalls);
    describe('TrackerController', function() {
        beforeEach(inject(function($injector) {
            $scope = $injector.get('$rootScope').$new();
            $q = $injector.get('$q');
            AuthServerProvider = $injector.get('AuthServerProvider');
            Tracker = $injector.get('Tracker');
          var locals = {
            '$scope': $scope,
            'AuthServerProvider': AuthServerProvider,
            'Tracker': Tracker,
          };
          createController = function() {
            $injector.get('$controller')('TrackerController',locals);
          };
          var deferred = $q.defer();
          deferred.resolve('resolveData');
          spyOn(Tracker, 'receive').and.returnValue(deferred.promise);
        }));
        beforeEach(function(){
          createController();
        });
        it('should call Tracker.receive', function(){
          expect(Tracker.receive).toHaveBeenCalled();
        });
        it('activities.length should be 1', function() {
          var activity = {
            page:'Notlogout',
            sessionId : 1
          };
          $scope.activities = [];
          $scope.showActivity(activity);
          expect($scope.activities.length).toBe(1);
        });
        it('activities.length should be 0', function() {
          var activity = {
            page:'logout',
            sessionId : 1
          };
          $scope.activities = [activity];
          $scope.showActivity(activity);
          expect($scope.activities.length).toBe(0);
        });
        it('activities.length should be 1', function() {
          var activity = {
            page:'Nlogout',
            sessionId : 1
          };
          $scope.activities = [activity];
          $scope.showActivity(activity);
          expect($scope.activities.length).toBe(1);
        });
      });
    });
