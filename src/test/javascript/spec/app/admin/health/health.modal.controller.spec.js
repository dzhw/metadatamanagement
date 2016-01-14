'use strict';

describe('Controllers Tests ', function () {
    var $scope, $uibModalInstance, createController;
        beforeEach(mockApiAccountCall);
        beforeEach(mockI18nCalls);
    describe('HealthModalController', function() {
        beforeEach(inject(function($injector) {
            $scope = $injector.get('$rootScope').$new();
            $uibModalInstance = {
              dismiss: jasmine.createSpy('$uibModalInstance.cancel'),
              result: {
                then: jasmine.createSpy('$uibModalInstance.result.then')
              }
            };
          var locals = {
            '$scope': $scope,
            '$uibModalInstance': $uibModalInstance,
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
        it('should call $uibModalInstance.dismiss', function() {
          $scope.cancel();
          expect($uibModalInstance.dismiss).toHaveBeenCalled();
        });
      });
    });
