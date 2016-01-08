'use strict';

describe('Controllers Tests ', function () {
    var $scope, $q, ConfigurationService, createController;
        beforeEach(mockApiAccountCall);
        beforeEach(mockI18nCalls);
    describe('ConfigurationController', function() {
        beforeEach(inject(function($injector) {
            $scope = $injector.get('$rootScope').$new();
            $q = $injector.get('$q');
            ConfigurationService = {
              get: function() {
              var deferred = $q.defer();
              deferred.resolve();
              return deferred.promise;
            }
          };
          var locals = {
            '$scope': $scope,
            'ConfigurationService': ConfigurationService
          };
          createController = function() {
            $injector.get('$controller')('ConfigurationController',locals);
          };
        }));
        beforeEach(function(){
          spyOn(ConfigurationService, 'get').and.callThrough();
          createController();
        });
        it('should call get', function() {
          ConfigurationService.get.and.returnValue($q.resolve());
          $scope.$apply(createController);
          expect(ConfigurationService.get).toHaveBeenCalled();
        });
      });
    });
