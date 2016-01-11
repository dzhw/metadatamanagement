'use strict';

describe('Controllers Tests ', function () {
    var $scope, $modalInstance, createController;
        beforeEach(mockApiAccountCall);
        beforeEach(mockI18nCalls);
    describe('HealthModalController', function() {
        beforeEach(inject(function($injector) {
            $scope = $injector.get('$rootScope').$new();
            $modalInstance = {
              dismiss: jasmine.createSpy('$modalInstance.cancel'),
              result: {
                then: jasmine.createSpy('$modalInstance.result.then')
              }
            };
          var locals = {
            '$scope': $scope,
            '$modalInstance': $modalInstance,
            'currentHealth': '/metadatamanagement/',
            'baseName':'testPage',
            'subSystemName':'metadatamanagement'
          };
          createController = function() {
            $injector.get('$controller')('HealthModalController',locals);
          };
        }));
        beforeEach(function(){
          createController();
        });
        it('should call $modalInstance.dismiss', function() {
          $scope.cancel();
          expect($modalInstance.dismiss).toHaveBeenCalled();
        });
      });
    });
