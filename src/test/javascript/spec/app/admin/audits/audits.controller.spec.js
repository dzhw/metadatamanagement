'use strict';

describe('Controllers Tests ', function () {
    var $scope, $q, AuditsService, createController,fromDate;
        beforeEach(mockApiAccountCall);
        beforeEach(mockI18nCalls);
    describe('AuditsController', function() {
        beforeEach(inject(function($injector) {
            $scope = $injector.get('$rootScope').$new();
            $q = $injector.get('$q');
            AuditsService = {
              findByDates: function() {
              var deferred = $q.defer();
              deferred.resolve();
              return deferred.promise;
            }
          };
          var locals = {
            '$scope': $scope,
            'AuditsService': AuditsService
          };
          createController = function() {
            $injector.get('$controller')('AuditsController',locals);
          };
        }));
        beforeEach(function(){
          spyOn(AuditsService, 'findByDates').and.callThrough();
          createController();
        });
        it('should call findByDates', function() {
          AuditsService.findByDates.and.returnValue($q.resolve());
          $scope.$apply(createController);
          expect(AuditsService.findByDates).toHaveBeenCalled();
        });
      });
    });
