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
            Tracker = {
              receive : function() {
                var deferred = $q.defer();
                deferred.resolve();
                deferred.reject({});
                return deferred.promise;
              }
            };
          var locals = {
            '$scope': $scope,
            'AuthServerProvider': AuthServerProvider,
            'Tracker': Tracker,

          };
          createController = function() {
            $injector.get('$controller')('TrackerController',locals);
          };
          spyOn(Tracker, 'receive').and.callThrough();
        }));
        beforeEach(function(){
          createController();
        });
        it('should call LogsService.findAll', function() {
            expect(Tracker.receive).toHaveBeenCalled();
        });
      });
    });
