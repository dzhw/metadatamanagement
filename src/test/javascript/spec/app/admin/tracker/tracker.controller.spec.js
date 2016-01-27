'use strict';

describe('Controllers Tests ', function () {
    var $scope, $q, AuthServerProvider, Tracker, createController;
    var activity = {
      page:'Notlogout',
      sessionId : 1
    };
        beforeEach(mockApiAccountCall);
        beforeEach(mockI18nCalls);
    describe('TrackerController', function() {
      beforeEach(function() {
        inject(function($controller, _$rootScope_, _AuthServerProvider_, _$q_) {
          $scope = _$rootScope_.$new();
          AuthServerProvider = _AuthServerProvider_;
          $q = _$q_;
          Tracker = {
            receive: function(){
              return {
                then: function(par1,par2,callback) {return callback(activity);}
              };
            }
          };
          var locals = {
            '$scope' : $scope,
            'AuthServerProvider' : AuthServerProvider,
            'Tracker': Tracker
          };
          createController = function() {
            return $controller('TrackerController', locals);
          };
        });
       });
        beforeEach(function(){
          spyOn(Tracker, 'receive').and.callThrough();
          createController();
            Tracker.receive();
        });
        it('should call Tracker.receive', function(){
          expect(Tracker.receive).toHaveBeenCalled();
        });
        it('activities.length should be 1', function() {
          activity.page = 'Notlogout';
          $scope.activities = [];
          $scope.showActivity(activity);
          expect($scope.activities.length).toBe(1);
        });
        it('activities.length should be 0', function() {
          activity.page = 'logout';
          $scope.activities = [activity];
          $scope.showActivity(activity);
          expect($scope.activities.length).toBe(0);
        });
        it('activities.length should be 1', function() {
          activity.page = 'Notlogout';
          $scope.activities = [activity];
          $scope.showActivity(activity);
          expect($scope.activities.length).toBe(1);
        });
      });
    });
